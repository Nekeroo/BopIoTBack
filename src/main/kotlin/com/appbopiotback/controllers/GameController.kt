package com.appbopiotback.controllers

import com.appbopiotback.models.Component
import com.appbopiotback.models.Game
import com.appbopiotback.models.SettingObject
import com.appbopiotback.models.enums.*
import com.appbopiotback.models.input.*
import com.appbopiotback.models.output.GameCreationResponse
import com.appbopiotback.models.output.GameIdResponse
import com.appbopiotback.models.saveGameInGames
import com.appbopiotback.repository.GameRepository
import com.appbopiotback.services.GameService
import io.netty.handler.codec.http.HttpResponseStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlinx.serialization.json.Json
import org.jboss.resteasy.reactive.RestPath


@Path("/games")
@Singleton
class GameController {

    var messageController: MessageController = MessageController()

    var gameService: GameService = GameService()

    @Inject
    lateinit var gameRepository : GameRepository

    companion object {
        var games = mutableListOf<Game>()
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    fun createGameFromData(gameInfosInput: GameInfosInput): Response {
        if (gameInfosInput != null) {
            val difficulty = DifficultyEnums.findDifficultyByID(gameInfosInput.difficulty)
            val game = Game(difficulty, gameInfosInput.topic, difficulty.lives, difficulty.timerLimit)
            game.id = gameService.generateUniqueId(games)
            games.add(game)
            messageController.subscribeOnTopic(game.topic) { jsonMessage ->
                try {
                    val message = Json.decodeFromString(MqttMessageActionResponse.serializer(), jsonMessage)
                    messageController.messageQueue.add(message)
                } catch (e: Exception) {
                    println("Erreur lors de la réception du message : ${e.message}")
                }
            }
            return Response.status(HttpResponseStatus.CREATED.code()).entity(GameCreationResponse(game)).build()
        }
        return Response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).build()
    }

    @GET
    @Path("/actions")
    fun getActions() : Response {
        val actions = ActionEnums.entries.filter { it.action.isActivated }
        return Response.status(Response.Status.OK).entity(actions).build()
    }

    @POST
    @Path("/start/{gameId}")
    fun gameStart(@RestPath gameId : Long) : Response {
        val game = games.find { it.id == gameId }
        if (game != null && game.actualStatus == GameStatus.PENDING.value){
            gameLogic(game)
            return Response.status(Response.Status.OK).entity("Game Started").build()
        }
        else {
            if (game == null) {
                return Response.status(Response.Status.FORBIDDEN).entity("Game not found").build()
            }
            else if (game.actualStatus != GameStatus.PENDING.value) {
                return Response.status(Response.Status.FORBIDDEN).entity("Game status : ${game.actualStatus}").build()
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build()
    }

    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGames() : Response {
        //val games = gameRepository.findAll() ?: return Response.status(Response.Status.NOT_FOUND).build()
        //return Response.status(Response.Status.OK).entity(games).build()
        return Response.status(Response.Status.OK).entity(games).build()
    }

    @GET
    @Path("/stop/{gameId}")
    fun stopCurrentGame(@RestPath gameId: Long) : Response {
        val game = games.find { it.id == gameId }
        if (game != null) {
            game.actualStatus = GameStatus.FINISHED.value
            game.finalStatus = ResultatStatus.DEFEAT.value
            games.saveGameInGames(game)
            messageController.sendMessage(MqttMessageEndGame(win = false), game.topic, MqttMessageEndGame.serializer())
            return Response.status(Response.Status.OK).entity("Game stopped").build()
        }
        return Response.status(Response.Status.NOT_FOUND).entity(gameId).build()
    }

    @GET
    @Path("/retrieve/{gameId}")
    fun retrieveGameById(@RestPath gameId : Long) : Response {
        //val game = gameRepository.findGameById(gameId)
          //  ?: return Response.status(Response.Status.NOT_FOUND).entity("GameId unknown").build()
        val game = games.find { it.id == gameId }
        return Response.status(Response.Status.OK).entity(game).build()
    }

    fun gameLogic(game: Game) {
        game.actualStatus = GameStatus.STARTED.value
        for (i in 0..20) {
            try {
                val actionAsked = ActionEnums.getRandomAction().action

                game.actions.add(actionAsked)
                messageController.sendMessage(MqttMessageActionForIoT(), game.topic, MqttMessageActionForIoT.serializer())
                messageController.sendMessage(MqttMessageAction(action = actionAsked), game.topic, MqttMessageAction.serializer())
                val startTime = System.currentTimeMillis()
                
                var response: MqttMessageActionResponse? = null

                while (System.currentTimeMillis() < startTime + game.timerLimit) {
                    response = messageController.messageQueue.poll()
                    if (response != null) break
                    Thread.sleep(100)
                }

                val elapsedTime = System.currentTimeMillis() - startTime

                if (response != null) {
                    if (gameService.isActionMadeOrNot(response.action, actionAsked, game, elapsedTime)) {
                        messageController.sendMessage(MqttMessageInfoAction(success = true, lives = game.lives), game.topic, MqttMessageInfoAction.serializer())
                    } else {
                        handleFools(game)
                        if (game.finalStatus == ResultatStatus.DEFEAT.value) break
                        messageController.sendMessage(MqttMessageInfoAction(success = false, lives = game.lives), game.topic, MqttMessageInfoAction.serializer())
                    }
                } else {
                    handleFools(game)
                    if (game.finalStatus == ResultatStatus.DEFEAT.value) break
                    messageController.sendMessage(MqttMessageErrorInfo(error = "No response found"), game.topic, MqttMessageErrorInfo.serializer())
                }
            } catch (e: Exception) {
                messageController.sendMessage(MqttMessageErrorInfo(error = e.message.toString()), game.topic, MqttMessageErrorInfo.serializer())
            }
        }

        if (game.lives > 0) {
            game.actualStatus = GameStatus.FINISHED.value
            game.finalStatus = ResultatStatus.WIN.value
            messageController.sendMessage(MqttMessageEndGame(win = true), game.topic, MqttMessageEndGame.serializer())
            games.saveGameInGames(game)
        }
    }

    fun handleFools(game : Game) {
        game.lives--
        if (gameService.isGameLost(game)) {
            game.actualStatus = GameStatus.FINISHED.value
            game.finalStatus = ResultatStatus.DEFEAT.value
            games.saveGameInGames(game)
            messageController.sendMessage(MqttMessageEndGame(win =  false), game.topic, MqttMessageEndGame.serializer())
        }
    }

    @POST
    @Path("/settings")
    @Consumes(MediaType.APPLICATION_JSON)
    fun modifySettingsForGame(settings: List<SettingObject>) {
        ComponentWithActions.desactivateComponentFromID(settings)
    }

}


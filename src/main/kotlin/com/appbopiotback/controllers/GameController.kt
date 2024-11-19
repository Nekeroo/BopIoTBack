package com.appbopiotback.controllers

import com.appbopiotback.models.Game
import com.appbopiotback.models.JsonMessage
import com.appbopiotback.models.enums.*
import com.appbopiotback.models.input.GameInfosInput
import com.appbopiotback.models.saveGameInGames
import com.appbopiotback.services.GameService
import io.netty.handler.codec.http.HttpResponseStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response


@Path("/game")
@Singleton
class GameController {

    @Inject
    lateinit var messageController: MessageController

    var gameService: GameService = GameService()

    companion object {
        var games = mutableListOf<Game>()
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    fun createGameFromData(gameInfosInput: GameInfosInput): Response {
        if (gameInfosInput != null) {
            val game = Game(DifficultyEnums.findDifficultyByID(gameInfosInput.difficulty), gameInfosInput.topic)
            games.add(game)
            println(games)
            return Response.status(HttpResponseStatus.CREATED.code()).build()
        }
        return Response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).build()
    }

    fun gameLogic(game: Game): Response {
        game.actualStatus = GameStatus.STARTED
        for (i in 0..20) {
            try {
                val actionAsked = ActionEnums.randomAction

                game.actions.add(actionAsked)
                messageController.sendMessage(JsonMessage(MessageTypeEnums.COMMAND, "send action"), game.topic)
                val startTime = System.nanoTime()

                val response = messageController.waitForResponse(game.topic)

                val elapsedTime = System.nanoTime() - startTime

                if (response != null) {
                    if (gameService.isActionMadeOrNot(response.action, actionAsked, game, elapsedTime)) {
                        break
                    } else {
                        game.survivalElements.lives--
                        if (gameService.isGameLost(game)) {
                            game.actualStatus = GameStatus.FINISHED
                            game.finalStatus = ResultatStatus.DEFEAT
                            games.saveGameInGames(game)
                            return Response.status(Response.Status.OK).entity("Game Over").build()
                        }
                    }
                } else {
                    Response.status(Response.Status.REQUEST_TIMEOUT).entity("No response received").build()
                }
            } catch (e: Exception) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build()
            }
        }

        game.actualStatus = GameStatus.FINISHED
        game.finalStatus = ResultatStatus.WIN
        games.saveGameInGames(game)
        return Response.status(Response.Status.OK).entity("WIN").build()

    }
}


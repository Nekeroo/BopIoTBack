package com.appbopiotback.models

import com.appbopiotback.controllers.GameController.Companion.games
import com.appbopiotback.models.enums.GameStatus
import com.appbopiotback.models.enums.ResultatStatus
import java.util.*

data class Game(
    // val user : User,
    val difficulty: Difficulty,
    val topic: String,
    val id : Long = Random().nextLong(10000),
    val survivalElements: SurvivalElements = SurvivalElements(difficulty.lives, difficulty.timerLimite),
    var actions: MutableList<Action> = mutableListOf(),
    var finalStatus : ResultatStatus = ResultatStatus.PENDING,
    var actualStatus : GameStatus = GameStatus.PENDING
)

fun List<Game>.saveGameInGames(game: Game) {
    games.replaceAll {
        if (it.id == game.id) {
            game
        } else {
            it
        }
    }
}

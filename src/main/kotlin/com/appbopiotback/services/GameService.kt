package com.appbopiotback.services

import com.appbopiotback.models.Action
import com.appbopiotback.models.Game
import com.appbopiotback.models.enums.ActionEnums
import com.appbopiotback.models.output.ActionResponse
import java.util.*

class GameService {

    fun isActionMadeOrNot(actionReceived : ActionResponse, actionExpected: Action, game: Game, timeElapsed: Long) : Boolean {
        return (actionReceived.id == actionExpected.id) && (timeElapsed < game.timerLimit)
    }

    fun isGameLost(game: Game) : Boolean {
        return game.lives == 0
    }

    fun retrieveActionFromGame(game : Game, actionId : Int) : Action? {
        val action = game.actions.find { it.id == actionId }
        return action
    }

    fun generateUniqueId(games: MutableList<Game>) : Long {
        var id: Long
        val random = Random()
        do {
            id = random.nextLong(10000)
        } while (games.any { it.id == id })
        return id
    }



}
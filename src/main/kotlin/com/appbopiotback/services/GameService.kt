package com.appbopiotback.services

import com.appbopiotback.models.Action
import com.appbopiotback.models.ActionResponse
import com.appbopiotback.models.Game
import com.appbopiotback.models.enums.ActionEnums

class GameService {

    fun getARandomAction() : Action {
        return ActionEnums.randomAction
    }

    fun isActionMadeOrNot(actionReceived : ActionResponse, actionExpected: Action, game: Game, timeElapsed: Long) : Boolean {
        return actionReceived.id == actionExpected.id && timeElapsed < game.survivalElements.timerLimit
    }

    fun isGameLost(game: Game) : Boolean {
        return game.survivalElements.lives == 0
    }


}
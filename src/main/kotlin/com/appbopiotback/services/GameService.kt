package com.appbopiotback.services

import com.appbopiotback.models.Action
import com.appbopiotback.models.Game
import com.appbopiotback.models.enums.ActionEnums
import com.appbopiotback.models.output.ActionResponse

class GameService {

    fun getARandomAction() : ActionEnums {
        return ActionEnums.randomAction
    }

    fun isActionMadeOrNot(actionReceived : ActionResponse, actionExpected: ActionEnums, game: Game, timeElapsed: Long) : Boolean {
        return actionReceived.id == actionExpected.action.id && timeElapsed < game.survivalElements.timerLimit
    }

    fun isGameLost(game: Game) : Boolean {
        return game.survivalElements.lives == 0
    }


}
package com.appbopiotback.models.enums

import com.appbopiotback.models.Action
import com.appbopiotback.models.Component
import com.appbopiotback.models.SettingObject
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
enum class ActionEnums ( var action : Action) {

    PUSH(Action(0, "Appuie !")),  // Pas de référence à Game ici
    RIGHT(Action( 1, "Penche à Droite !")),
    LEFT(Action(2, "Penche à Gauche !")),
    DOUBLE_CLAP(Action(3,  "Tape 2 fois dans tes Mains !")),
    RIGHT_JOYSTICK(Action(4, "Tourne le Joystick à Droite !")),
    LEFT_JOYSTICK(Action(5,  "Tourne le Joystick à Gauche !")),
    UP_JOYSTICK(Action(6,  "Tourne le Joystick en Haut !")),
    DOWN_JOYSTICK(Action(7, "Tourne le Joystick en Bas !"));

    companion object {

        fun getRandomAction(): ActionEnums {
            val activeActions = entries.filter { it.action.isActivated }

            if (activeActions.isEmpty()) {
                throw IllegalStateException("No active actions available")
            }

            return activeActions[Random().nextInt(activeActions.size)]
        }
    }

}

enum class ComponentWithActions(var component: Component) {
    MICROPHONE(Component(1, mutableListOf(ActionEnums.DOUBLE_CLAP))),
    ACCELERO(Component(2, mutableListOf(ActionEnums.LEFT, ActionEnums.RIGHT))),
    JOYSTICK_PUSH(Component(3, mutableListOf(ActionEnums.PUSH))),
    JOYSTICK_MOVES(Component(4, mutableListOf(ActionEnums.UP_JOYSTICK, ActionEnums.LEFT_JOYSTICK, ActionEnums.RIGHT_JOYSTICK, ActionEnums.DOWN_JOYSTICK)));

    companion object {
        fun desactivateComponentFromID(settings : List<SettingObject>) {
            for (setting in settings) {
                if (!setting.isActivated)
                when(setting.id) {
                    1 -> MICROPHONE.component.actions.forEach { actionEnum -> actionEnum.action.isActivated = false }
                    2 -> ACCELERO.component.actions.forEach { actionEnum -> actionEnum.action.isActivated = false }
                    3 -> JOYSTICK_PUSH.component.actions.forEach { actionEnum -> actionEnum.action.isActivated = false }
                    4 -> JOYSTICK_MOVES.component.actions.forEach { actionEnum -> actionEnum.action.isActivated = false }
                }
            }
        }
    }
}
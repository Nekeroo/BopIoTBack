package com.appbopiotback.models.enums

import com.appbopiotback.models.Action
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
enum class ActionEnums ( val action : Action) {

    PUSH(Action(0, "Appuie !")),
    RIGHT(Action(1, "Penche à Droite !")),
    LEFT(Action(2, "Penche à Gauche !")),
    DOUBLE_CLAP(Action(3, "Tape 2 fois dans tes Mains ! ")),
    RIGHT_JOYSTICK(Action(4, "Tourne le Joystick à Droite !")),
    LEFT_JOYSTICK(Action(5, "Tourne le Joystick à Gauche !")),
    UP_JOYSTICK(Action(6,"Tourne le Joystick en Haut !")),
    DOWN_JOYSTICK(Action(7, "Tourne le Joystick en Bas !"));

    companion object {
        val randomAction : ActionEnums
            get() {
                return entries[Random().nextInt(8)]
            }
    }

}
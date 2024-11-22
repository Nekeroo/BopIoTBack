package com.appbopiotback.models.enums

import kotlinx.serialization.Serializable

@Serializable
enum class MessageTypeEnums (val id : Int) {

    ERROR(0),

    ACTION(1),

    /*
    {
        "type" : 1,
        "action" : {
            "id": 1
            "label": "Appuie boutton .."
        }
    }
     */
    RESPONSE(2),

    INFO_ACTION(3), // INFORMATION ACTION (Action OK / Action KO)
    /*
    {
        "type" : 3,
        "success" : true / false
    }
     */

    FINAL(9) // STATUS FINAL DE LA GAME (Défaite / Victoire)
    /*
       {
        "type" : 9,
        "win": true / false
       }
     */

}
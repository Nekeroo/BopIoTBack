package com.appbopiotback.models.input

import com.appbopiotback.models.enums.ActionEnums
import com.appbopiotback.models.output.ActionResponse
import kotlinx.serialization.Serializable

@Serializable
data class MqttMessageAction( val type : Int, val action : ActionEnums)

@Serializable
data class MqttMessageActionResponse( val type : Int, val action : ActionResponse)

@Serializable
data class MqttMessageLibelle( val type: Int, val label : String)

@Serializable
data class MqttMessageInfoAction( val type : Int, val success : Boolean)

@Serializable
data class MqttMessageEndGame( val type : Int, val win : Boolean)
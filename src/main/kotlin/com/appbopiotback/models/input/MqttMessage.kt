package com.appbopiotback.models.input

import com.appbopiotback.models.Action
import com.appbopiotback.models.enums.MessageTypeEnums
import com.appbopiotback.models.output.ActionResponse
import kotlinx.serialization.Serializable

@Serializable
data class MqttMessageAction( val type : Int = MessageTypeEnums.ACTION.id, val action : Action)

@Serializable
data class MqttMessageActionForIoT(val type : Int = MessageTypeEnums.ACTION.id)

@Serializable
data class MqttMessageActionResponse( val type : Int = MessageTypeEnums.RESPONSE.id, val action : ActionResponse)

@Serializable
data class MqttMessageLibelle( val type: Int, val label : String)

@Serializable
data class MqttMessageInfoAction( val type : Int = MessageTypeEnums.INFO_ACTION.id, val success : Boolean)

@Serializable
data class MqttMessageEndGame( val type : Int = MessageTypeEnums.FINAL.id, val win : Boolean)

@Serializable
data class MqttMessageErrorInfo( val type : Int = MessageTypeEnums.ERROR.id, val error : String)
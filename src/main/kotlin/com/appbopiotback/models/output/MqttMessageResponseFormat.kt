package com.appbopiotback.models.output

import kotlinx.serialization.Serializable

@Serializable
data class MqttMessageResponseFormat(
    val type: Int,
    val action : ActionResponse
)

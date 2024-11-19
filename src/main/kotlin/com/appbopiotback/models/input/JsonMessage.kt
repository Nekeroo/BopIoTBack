package com.appbopiotback.models.input

import com.appbopiotback.models.enums.MessageTypeEnums
import kotlinx.serialization.Serializable

@Serializable
data class JsonMessage(
    val type: MessageTypeEnums,
    val message: String
)
package com.appbopiotback.models

import com.appbopiotback.models.enums.MessageTypeEnums

data class JsonMessage(
    val type: MessageTypeEnums,
    val message: String
)
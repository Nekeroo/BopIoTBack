package com.appbopiotback.models.output

import kotlinx.serialization.Serializable

@Serializable
data class GameIdResponse(
    val gameId : Long
)

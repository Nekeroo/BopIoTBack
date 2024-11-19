package com.appbopiotback.models.input

import com.appbopiotback.models.Difficulty
import com.appbopiotback.models.User

data class GameInfosInput (
    //val user : User,
    val difficulty: Int,
    val topic: String
)
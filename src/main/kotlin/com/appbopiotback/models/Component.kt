package com.appbopiotback.models

import com.appbopiotback.models.enums.ActionEnums
import kotlinx.serialization.Serializable

@Serializable
data class Component (
    val id : Int,
    val actions : List<ActionEnums>
)
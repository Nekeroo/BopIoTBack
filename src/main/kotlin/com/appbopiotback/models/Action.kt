package com.appbopiotback.models

import com.appbopiotback.models.enums.ActionStatus
import kotlinx.serialization.Serializable

data class Action(
    val id : Int,
    val label: String,
    val actionStatus : ActionStatus = ActionStatus.NA
)

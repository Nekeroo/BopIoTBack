package com.appbopiotback.models

import com.appbopiotback.models.enums.ActionStatus

data class Action(
    val id : Int,
    val label: String,
    val actionStatus : ActionStatus = ActionStatus.NA
)

data class ActionResponse(
    val id : Int
)
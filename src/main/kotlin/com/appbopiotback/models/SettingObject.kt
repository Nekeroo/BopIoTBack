package com.appbopiotback.models

import kotlinx.serialization.Serializable

@Serializable
data class SettingObject(
    var id: Int,
    var isActivated : Boolean
)

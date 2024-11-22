package com.appbopiotback.models

import com.appbopiotback.models.enums.ActionStatus
import jakarta.persistence.*
import kotlinx.serialization.Serializable

//@Entity
//@Table(name = "action")
@Serializable
data class Action(
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id", nullable = false)
    var id : Int,
    //@Column(name="label")
    var label: String,
    //@Enumerated(EnumType.ORDINAL)
    //@Column(name = "action_status"),
    var isActivated : Boolean = true,
    val actionStatus : ActionStatus = ActionStatus.NA,
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "game_id", nullable = false)
) {

}

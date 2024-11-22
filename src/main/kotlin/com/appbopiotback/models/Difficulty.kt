package com.appbopiotback.models

import jakarta.persistence.*

//@Table(name = "difficulty")
//@Entity
data class Difficulty(
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int,
    //@Column(name = "label")
    val label : String,
    //@Column(name = "lives")
    val lives : Int,
    //@Column(name = "timer_limit")
    val timerLimit : Int,
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "difficulty")
    val games: List<Game> = listOf()
)
{
    constructor() : this(0, "test", 0, 0, listOf())
}

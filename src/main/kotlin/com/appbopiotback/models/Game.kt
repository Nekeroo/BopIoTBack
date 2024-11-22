package com.appbopiotback.models

import com.appbopiotback.controllers.GameController.Companion.games
import com.appbopiotback.models.enums.GameStatus
import com.appbopiotback.models.enums.ResultatStatus
import jakarta.persistence.*
import java.util.*

//@Entity
//@Table(name = "game")
data class Game(
    // val user : User,
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "difficulty_id", nullable = false)
    val difficulty: Difficulty,
    // @Column(name = "topic")
    val topic: String,
    //@Column(name = "lives")
    var lives : Int,
    //@Column(name = "timer_limit")
    val timerLimit : Int,
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0,
    //@Column(name = "final_status")
    var finalStatus : Int = ResultatStatus.PENDING.value,
    //@Column(name = "actual_status")
    var actualStatus : Int = GameStatus.PENDING.value,
    //@Column(name = "actions")
    //@OneToMany
    var actions: MutableList<Action> = mutableListOf(),
) {
}

fun List<Game>.saveGameInGames(game: Game) {
    games.replaceAll {
        if (it.id == game.id) {
            game
        } else {
            it
        }
    }
}

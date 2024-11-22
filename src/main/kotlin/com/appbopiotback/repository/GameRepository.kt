package com.appbopiotback.repository

import com.appbopiotback.models.Game
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class GameRepository : PanacheRepository<Game> {
    fun findGameById(id: Long): Game? {
        return find("id", id).firstResult()
    }

    fun findAllGames(): List<Game> {
        return listAll()
    }
}
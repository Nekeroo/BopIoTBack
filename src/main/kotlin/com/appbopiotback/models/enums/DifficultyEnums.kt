package com.appbopiotback.models.enums

import com.appbopiotback.models.Difficulty

enum class DifficultyEnums (val difficulty: Difficulty) {

    FACILE(Difficulty(0, "Facile", 5, 7000)),
    MOYEN(Difficulty(1, "Moyen", 3, 5000)),
    DIFFICILE(Difficulty(2, "Difficile", 1, 3000));

    companion object {
       fun findDifficultyByID(difficultyID: Int): Difficulty {
           return DifficultyEnums.entries[difficultyID].difficulty
       }
    }

}
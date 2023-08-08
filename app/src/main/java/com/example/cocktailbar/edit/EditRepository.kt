package com.example.cocktailbar.edit

import com.example.cocktailbar.db.Cocktail
import com.example.cocktailbar.db.Database

class EditRepository {
    private val cocktailDao = Database.instance.cocktailDao()
    suspend fun addItem(cocktail: Cocktail) {
        cocktailDao.addCocktail(cocktail)
    }

    suspend fun getItem(id: Int): Cocktail {
        return cocktailDao.getCocktailsById(id)
    }
}
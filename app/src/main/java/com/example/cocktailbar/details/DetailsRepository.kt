package com.example.cocktailbar.details

import com.example.cocktailbar.db.Cocktail
import com.example.cocktailbar.db.Database

class DetailsRepository {
    private val cocktailDao = Database.instance.cocktailDao()
    suspend fun getItemById(id: Int): Cocktail {
        return cocktailDao.getCocktailsById(id)
    }
}
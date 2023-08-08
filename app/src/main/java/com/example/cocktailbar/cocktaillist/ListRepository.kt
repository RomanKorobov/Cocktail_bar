package com.example.cocktailbar.cocktaillist

import com.example.cocktailbar.db.Cocktail
import com.example.cocktailbar.db.Database

class ListRepository {
    private val cocktailDao = Database.instance.cocktailDao()
    suspend fun getList(): List<Cocktail> {
        return cocktailDao.getCocktails()
    }
}
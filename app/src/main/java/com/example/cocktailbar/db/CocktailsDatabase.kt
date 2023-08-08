package com.example.cocktailbar.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cocktail::class], version = CocktailsDatabase.DB_VERSION)
abstract class CocktailsDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "Cocktails-database"
    }
}

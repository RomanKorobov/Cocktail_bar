package com.example.cocktailbar.db

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: CocktailsDatabase
        private set

    fun init(context: Context) {
        instance =
            Room.databaseBuilder(context, CocktailsDatabase::class.java, CocktailsDatabase.DB_NAME)
                .build()
    }

}
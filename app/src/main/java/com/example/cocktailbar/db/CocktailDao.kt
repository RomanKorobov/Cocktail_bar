package com.example.cocktailbar.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCocktail(cocktail: Cocktail)

    @Query("SELECT * FROM CocktailList WHERE id =:cocktailId")
    suspend fun getCocktailsById(cocktailId: Int): Cocktail

    @Query("SELECT * FROM CocktailList")
    suspend fun getCocktails(): List<Cocktail>

    @Query("DELETE FROM CocktailList WHERE id =:cocktailId")
    suspend fun deleteCocktail(cocktailId: Int)
}
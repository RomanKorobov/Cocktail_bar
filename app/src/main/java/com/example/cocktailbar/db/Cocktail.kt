package com.example.cocktailbar.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CocktailList")
data class Cocktail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "recipe")
    val recipe: String?,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "ingredients")
    val ingredients: String?
)
package com.example.cocktailbar

import android.app.Application
import com.example.cocktailbar.db.Database

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(applicationContext)
    }

}
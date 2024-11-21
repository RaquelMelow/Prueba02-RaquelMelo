package com.example.prueba02_raquelmelo

import android.app.Application
import androidx.room.Room
import com.example.prueba02_raquelmelo.database.PostsDatabase

class App: Application() {

    companion object {
        lateinit var db: PostsDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            PostsDatabase::class.java,
            "posts_db"
        ).build()
    }
}
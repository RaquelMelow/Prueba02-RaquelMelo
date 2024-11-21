package com.example.prueba02_raquelmelo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1)
abstract class PostsDatabase: RoomDatabase() {
    abstract val postDao: PostDao
}
package com.example.prueba02_raquelmelo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE title LIKE :pattern")
    fun filterPosts(pattern: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)
}
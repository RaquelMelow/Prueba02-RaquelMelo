package com.example.prueba02_raquelmelo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.prueba02_raquelmelo.domain.model.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)

fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}

fun PostEntity.toDomain(): Post {
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}
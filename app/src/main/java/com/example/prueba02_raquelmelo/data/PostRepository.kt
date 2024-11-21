package com.example.prueba02_raquelmelo.repository

import com.example.prueba02_raquelmelo.App
import com.example.prueba02_raquelmelo.database.toDomain
import com.example.prueba02_raquelmelo.database.toEntity
import com.example.prueba02_raquelmelo.database.PostDao
import com.example.prueba02_raquelmelo.domain.model.Post
import com.example.prueba02_raquelmelo.domain.util.ApiResult
import com.example.prueba02_raquelmelo.domain.util.map
import com.example.prueba02_raquelmelo.network.ApiService
import com.example.prueba02_raquelmelo.network.DataError
import com.example.prueba02_raquelmelo.network.RetrofitInstance
import com.example.prueba02_raquelmelo.network.safeCall
import com.example.prueba02_raquelmelo.network.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class PostsRepository() {
    private val postService = RetrofitInstance.retrofit.create(ApiService::class.java)
    private val dao: PostDao = App.db.postDao

    fun getPosts(): Flow<ApiResult<List<Post>, DataError.Network>> {
        return flow {

            val localPosts = dao.getPosts().first()

            emit(ApiResult.Success(localPosts.map { it.toDomain() }))

            if (localPosts.isEmpty()) {
                when(val result = fetchPosts()) {
                    is ApiResult.Error -> {
                        emit(ApiResult.Error(result.error))
                    }

                    is ApiResult.Success -> {
                        dao.upsertPosts(result.data.map { it.toEntity() })
                        emit(ApiResult.Success(result.data))
                    }
                }
            }
        }
    }

    suspend fun fetchPosts(): ApiResult<List<Post>, DataError.Network> {
        return safeCall {
            postService.getPosts()
        }.map { posts ->
            posts.map {
                it.toDomain()
            }
        }
    }

    suspend fun getPost(id: Int): ApiResult<Post, DataError.Network> {
        return safeCall {
            postService.getPost(id)
        }.map {
            it.toDomain()
        }
    }
}

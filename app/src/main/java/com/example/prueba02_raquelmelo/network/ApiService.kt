package com.example.prueba02_raquelmelo.network

import com.example.prueba02_raquelmelo.domain.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): Response<List<PostResponse>>

    @GET("/posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): Response<PostResponse>
}
package com.example.testeapp.api

import com.example.testeapp.model.Post
import com.example.testeapp.model.Repositories
import com.example.testeapp.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface XApi {
    @GET("search/repositories")
    suspend fun getRepositoriesPost(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<Repositories>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<User>
}
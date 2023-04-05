package com.example.testeapp.data.repositores

import com.example.testeapp.model.*
import retrofit2.Response
import retrofit2.Retrofit

interface RemoteRepository {

    suspend fun fetchPostList(page: Int): Result<Repositories>
    suspend fun fetchAuthorInfo(userId: String): Result<User>
    suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T>
    fun parseError(response: Response<*>, retrofit: Retrofit): Error?

}
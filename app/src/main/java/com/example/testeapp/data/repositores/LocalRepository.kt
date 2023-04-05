package com.example.testeapp.data.repositores

import com.example.testeapp.model.PostWithUser

interface LocalRepository {
    fun getPostsCache(): List<PostWithUser>
    fun savePosts(posts: List<PostWithUser>)
    fun savePost(post: PostWithUser)
    suspend fun updatePost(post: PostWithUser)
    fun deleteAll()
}
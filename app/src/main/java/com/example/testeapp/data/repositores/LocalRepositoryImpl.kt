package com.example.testeapp.data.repositores

import com.example.testeapp.data.PostDao
import com.example.testeapp.model.Post
import com.example.testeapp.model.PostWithUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(private val postDao: PostDao) : LocalRepository {
    override fun getPostsCache(): List<PostWithUser> {
        return postDao.getAll()
    }

    override fun savePosts(posts: List<PostWithUser>) {
        postDao.insertAll(posts)
    }

    override fun savePost(post: PostWithUser) {
        postDao.insert(post)
    }

    suspend override fun updatePost(post: PostWithUser) {
        postDao.update(post)
    }

    override fun deleteAll() {
        postDao.deleteAll()
    }
}
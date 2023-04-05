package com.example.testeapp.data

import androidx.room.*
import com.example.testeapp.model.Post
import com.example.testeapp.model.PostWithUser

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostWithUser)

    @Query("SELECT * FROM posts order by id asc")
    fun getAll(): List<PostWithUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: Collection<PostWithUser>)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Update
    suspend fun update(post: PostWithUser)
}
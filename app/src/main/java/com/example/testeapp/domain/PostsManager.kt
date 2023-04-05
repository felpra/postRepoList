package com.example.testeapp.domain

import com.example.testeapp.data.repositores.LocalRepository
import com.example.testeapp.data.repositores.RemoteRepository
import com.example.testeapp.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.testeapp.common.areListsEqual
import com.example.testeapp.model.PostWithUser
import javax.inject.Inject

class PostsManager @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    suspend fun getPosts(page: Int): Flow<Result<Any>> {
        return flow {
            emit(Result.inProgress())
            val cache = localRepository.getPostsCache()
            val remote = remoteRepository.fetchPostList(page)

            if (remote.status == Result.Status.SUCCESS) {
                remote.data?.let {
                    val postsWithUsers = mutableListOf<PostWithUser>()
                    it?.items?.forEach { post ->
                        var users = remoteRepository.fetchAuthorInfo(post.owner.login)
                        if (users.status == Result.Status.SUCCESS) {
                            users.data?.let {
                                postsWithUsers.add(
                                    PostWithUser(
                                        post.id,
                                        post.name,
                                        post.full_name,
                                        it,
                                        post.forks
                                    )
                                )
                            }
                        }
                    }

                    if (!cache.areListsEqual(it.items)) {
                        val updatedWithFavorites = updateFavorites(cache, postsWithUsers)
                        localRepository.deleteAll()
                        localRepository.savePosts((updatedWithFavorites + cache).distinct())
                        emit(Result.success(updatedWithFavorites))
                    } else
                        emit(Result.success(cache))
                }
            } else {
                if (!cache.isNullOrEmpty())
                    emit(Result.success(cache))
                else
                    emit(remote)
            }


        }.flowOn(Dispatchers.IO)
    }

    suspend fun updatePost(post: PostWithUser) {
        localRepository.updatePost(post.copy(isFavorite = true))
    }

    fun updateFavorites(
        postsA: List<PostWithUser>,
        postsB: List<PostWithUser>
    ): List<PostWithUser> {
        return postsB.map { postB ->
            val postA = postsA.find { it.id == postB.id }
            if (postA != null) {
                postB.copy(isFavorite = postA.isFavorite)
            } else {
                postB
            }
        }
    }

}

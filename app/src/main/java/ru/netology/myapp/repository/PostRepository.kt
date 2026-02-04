package ru.netology.myapp.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.myapp.dto.Post

interface PostRepository {
    val data: Flow<PagingData<Post>>

    suspend fun getAll(): List<Post>

    suspend fun likeById(id: Long, likedByMe: Boolean): Post

    suspend fun removeById(id: Long)

    suspend fun save(post: Post): Post
}

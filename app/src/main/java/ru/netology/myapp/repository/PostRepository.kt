package ru.netology.myapp.repository

import ru.netology.myapp.dto.Post

interface PostRepository {

    fun getAllAsync(callback: PostCallback<List<Post>>)

    fun likeById(id: Long, likedByMe: Boolean, callback: PostCallback<Post>)

    fun removeById(id: Long, callback: PostCallback<Unit>)

    fun save(post: Post, callback: PostCallback<Post>)

    interface PostCallback<T> {
        fun onSuccess(result: T)
        fun onError(error: Throwable)
    }
}

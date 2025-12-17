package ru.netology.myapp.repository

import ru.netology.myapp.dto.Post

interface PostRepository {

    fun get(): List<Post>
    fun like(post: Post): Post
    fun repost(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
    fun editCancel(post: Post)

    fun getAllAsync(callback: GetaAllCallback)

    fun likeAsync(post: Post, callback: LikeCallback)
    fun saveAsync(post: Post, callback: SaveCallback)

    interface GetaAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface LikeCallback {
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface SaveCallback {
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }
}
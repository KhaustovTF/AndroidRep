package ru.netology.myapp.dao

import ru.netology.myapp.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun repostById(id: Long)
    fun removeById(id: Long)
}
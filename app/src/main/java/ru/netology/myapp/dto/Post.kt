package ru.netology.myapp.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likesCount: Int = 0,
    val likesByMe: Boolean = false,
    var repostCount: Int = 0,
    val video: String? = null
)

package ru.netology.myapp.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likesCount: Int = 0,
    var likesByMe: Boolean = false,
    var repostCount: Int = 0,
    var repostByMe: Boolean = false
)

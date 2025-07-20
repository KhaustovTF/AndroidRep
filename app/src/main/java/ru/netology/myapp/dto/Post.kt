package ru.netology.myapp.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likesCount: Int = 9999,
    val likesByMe: Boolean = false,
    var repostCount: Int = 999999,
    val repostByMe: Boolean = false
)

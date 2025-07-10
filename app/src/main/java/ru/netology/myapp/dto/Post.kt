package ru.netology.myapp.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var likesByMe: Boolean = false
)

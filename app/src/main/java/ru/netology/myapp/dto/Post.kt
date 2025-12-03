package ru.netology.myapp.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val video: String? = null,


    @SerializedName("likes")
    var likesCount: Int = 0,

    @SerializedName("likedByMe")
    val likesByMe: Boolean = false,

    @SerializedName("shares")
    var repostCount: Int = 0,
)

package ru.netology.myapp.model

import ru.netology.myapp.dto.Post

data class FeedModel(
    val post: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false
)

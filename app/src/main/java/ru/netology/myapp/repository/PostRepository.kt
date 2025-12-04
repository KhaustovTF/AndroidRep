package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post

interface PostRepository {
    fun get(): List<Post>
    fun  like(post: Post): Post
    fun repost(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post
    fun editCancel(post: Post)

    //what to do with that ssh

}
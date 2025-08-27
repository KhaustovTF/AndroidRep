package ru.netology.myapp

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun  like(id: Long)
    fun repost(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
    fun editCancel(post: Post)

    //what to do with that ssh

}
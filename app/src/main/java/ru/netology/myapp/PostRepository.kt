package ru.netology.myapp

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun  like(id: Long)
    fun repost(id: Long)

    //what to do with that ssh

}
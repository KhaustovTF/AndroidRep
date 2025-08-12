package ru.netology.myapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.myapp.dto.Post

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data: LiveData<Post> = repository.get()

    fun like() = repository.like()

    fun repost() = repository.repost()

}
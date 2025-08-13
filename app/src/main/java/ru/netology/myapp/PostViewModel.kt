package ru.netology.myapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.myapp.dto.Post

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data: LiveData<List<Post>> = repository.get()

    fun like(id: Long) = repository.like(id)

    fun repost(id: Long) = repository.repost(id)

}
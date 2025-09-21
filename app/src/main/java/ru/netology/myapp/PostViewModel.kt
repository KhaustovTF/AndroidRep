package ru.netology.myapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.myapp.dto.Post
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryFileDZImpl
import ru.netology.myapp.repository.PostRepositoryFileImpl
import ru.netology.myapp.repository.PostRepositoryInMemoryImpl
import ru.netology.myapp.repository.PostRepositorySharedPrefImpl

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likesByMe = false
)
class PostViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryFileDZImpl(application)

    val data: LiveData<List<Post>> = repository.get()
    val edited = MutableLiveData(empty)

    fun like(id: Long) = repository.like(id)

    fun repost(id: Long) = repository.repost(id)

    fun removeById (id: Long) = repository.removeById(id)

    fun changeContent(content: String){
        val text = content.trim()
        edited.value?.let {
            if (text == it.content){
                return@let
            }

            edited.value = it.copy(content = text)
        }
    }

    fun save(){
        edited.value.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun editCancel(){
        edited.value = empty
    }

    fun edit(post: Post){
        edited.value = post
    }
}
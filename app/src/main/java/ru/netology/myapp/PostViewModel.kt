package ru.netology.myapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post
import ru.netology.myapp.model.FeedModel
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.repository.PostRepositoryNetwork
import ru.netology.myapp.util.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likesByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryNetwork()
    private val _data: MutableLiveData<FeedModel> = MutableLiveData(FeedModel())
    val data: MutableLiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun like(id: Long) = repository.like(id)

    fun repost(id: Long) = repository.repost(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun changeContent(content: String) {
        val text = content.trim()
        edited.value?.let {
            if (text == it.content) {
                return@let
            }

            edited.value = it.copy(content = text)
        }
    }

    init {
        load()
    }

    fun save(toString: String) {
        thread {
            edited.value?.let {

                repository.save(it)

                _postCreated.postValue(Unit)
            }

            edited.postValue(empty)
        }
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val post = repository.get()
                _data.postValue(FeedModel(post = post, empty = post.isEmpty()))
            } catch (_: Exception) {
                _data.postValue(FeedModel(error = true))
            }


        }
    }

    fun editCancel() {
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }
}
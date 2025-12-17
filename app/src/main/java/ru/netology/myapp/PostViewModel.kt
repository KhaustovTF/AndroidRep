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

    init {
        load()
    }

    fun load() {
        _data.value = FeedModel(loading = true)

        repository.getAllAsync(object : PostRepository.PostCallback<List<Post>> {
            override fun onSuccess(result: List<Post>) {
                _data.postValue(FeedModel(post = result, empty = result.isEmpty()))
            }

            override fun onError(error: Throwable) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun like(post: Post) {
        repository.likeById(post.id, post.likesByMe, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(result: Post) {
                val current = _data.value ?: FeedModel()
                val newPosts = current.post.map { if (it.id == result.id) result else it }
                _data.postValue(current.copy(post = newPosts, empty = newPosts.isEmpty(), error = false))
            }

            override fun onError(error: Throwable) {
                val current = _data.value ?: FeedModel()
                _data.postValue(current.copy(error = true))
            }
        })
    }

    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.PostCallback<Unit> {
            override fun onSuccess(result: Unit) {
                val current = _data.value ?: FeedModel()
                val newPosts = current.post.filter { it.id != id }
                _data.postValue(current.copy(post = newPosts, empty = newPosts.isEmpty(), error = false))
            }

            override fun onError(error: Throwable) {
                val current = _data.value ?: FeedModel()
                _data.postValue(current.copy(error = true))
            }
        })
    }


    fun save() {
        val post = edited.value ?: return

        repository.save(post, object : PostRepository.PostCallback<Post> {
            override fun onSuccess(result: Post) {
                _postCreated.postValue(Unit)
                edited.postValue(empty)
            }

            override fun onError(error: Throwable) {
                val current = _data.value ?: FeedModel()
                _data.postValue(current.copy(error = true, loading = false))
            }
        })
    }

    fun changeContent(content: String) {
        val text = content.trim()
        edited.value?.let {
            if (text == it.content) return@let
            edited.value = it.copy(content = text)
        }
    }

    fun editCancel() {
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun repost(id: Long) {

    }
}

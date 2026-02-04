package ru.netology.myapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.myapp.dto.Post
import ru.netology.myapp.repository.PostRepository
import ru.netology.myapp.util.SingleLiveEvent
import javax.inject.Inject

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likesByMe = false
)

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {


    val data: Flow<PagingData<Post>> = repository.data.cachedIn(viewModelScope)

    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated


    private val _refresh = SingleLiveEvent<Unit>()
    val refresh: LiveData<Unit>
        get() = _refresh


    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean>
        get() = _error

    fun like(post: Post) = viewModelScope.launch {
        runCatching { repository.likeById(post.id, post.likesByMe) }
            .onSuccess {
                _error.postValue(false)
                _refresh.postValue(Unit)
            }
            .onFailure {
                _error.postValue(true)
            }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        runCatching { repository.removeById(id) }
            .onSuccess {
                _error.postValue(false)
                _refresh.postValue(Unit)
            }
            .onFailure {
                _error.postValue(true)
            }
    }

    fun save() {
        val post = edited.value ?: return
        viewModelScope.launch {
            runCatching { repository.save(post) }
                .onSuccess {
                    _postCreated.postValue(Unit)
                    edited.postValue(empty)
                    _error.postValue(false)
                    _refresh.postValue(Unit)
                }
                .onFailure {
                    _error.postValue(true)
                }
        }
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

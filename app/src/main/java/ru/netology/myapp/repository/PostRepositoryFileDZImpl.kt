package ru.netology.myapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.myapp.dto.Post

class PostRepositoryFileDZImpl(private val context: Context) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val file = context.filesDir.resolve(FILE_NAME)

    private var nextId = 1L

    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = value
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        if (file.exists()) {
            file.bufferedReader().use { reader ->
                posts = gson.fromJson(reader, type)
                nextId = (posts.maxOfOrNull { it.id } ?: 0L) + 1
            }
        } else {
            sync()
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "now"
                )
            ) + posts
        } else {
            posts.map {
                if (it.id == post.id) it.copy(content = post.content) else it
            }
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likesByMe = !post.likesByMe,
                    likesCount = if (post.likesByMe) post.likesCount - 1 else post.likesCount + 1
                )
            } else post
        }
    }

    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    repostByMe = !post.repostByMe,
                    repostCount = post.repostCount + 1
                )
            } else post
        }
    }

    override fun editCancel(post: Post) {

    }

    private fun sync() {
        file.bufferedWriter().use { writer ->
            writer.write(gson.toJson(posts))
        }
    }

    private companion object {
        private const val FILE_NAME = "posts.json"
    }
}

package ru.netology.myapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    private var posts: List<Post> = emptyList()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)

        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map { if (it.id == id) saved else it }
        }
        data.value = posts
    }

    override fun like(id: Long) {
        dao.likeById(id)
        posts = posts.map { p ->
            if (p.id != id) p
            else p.copy(
                likesByMe = !p.likesByMe,
                likesCount = if (p.likesByMe) p.likesCount - 1 else p.likesCount + 1
            )
        }
        data.value = posts
    }

    override fun repost(id: Long) {
        dao.repostById(id)
        posts = posts.map { p ->
            if (p.id != id) p else p.copy(repostCount = p.repostCount + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun editCancel(post: Post) {
    }
}

package ru.netology.myapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.myapp.dto.Post

class PostRepositorySharedPrefImpl(context: Context) : PostRepository {

    private val pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private var index: Long = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        pref.getString(POSTS_KEY, null)?.let { json ->
            posts = gson.fromJson(json, type)
            index = (posts.maxOfOrNull { post -> post.id } ?: 0) + 1
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }


    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = index++,
                    author = "Me",
                    published = "now"
                )
            ) + posts
        } else {
            posts.map {
                if (post.id == it.id) {
                    it.copy(content = post.content)
                } else it

            }
        }




    }

    override fun editCancel(post: Post) {
        posts.map {
            it.copy(content = post.content)
        }

    }



    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likesByMe = !post.likesByMe,
                    likesCount = if (post.likesByMe) post.likesCount - 1 else post.likesCount + 1
                )
            } else {
                post
            }
        }


    }

    //            if (posts.likesByMe == false) {
//                post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount + 1)
//                data.value = post
//            } else {
//                post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount - 1)
//                data.value = post
//            }


    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    repostByMe = !post.repostByMe, repostCount = post.repostCount + 1
                )
            } else {
                post
            }
        }



    }

    private fun sync(){
        pref.edit().apply()   {
            putString(POSTS_KEY, gson.toJson(posts, type))
            apply()
        }
    }

        companion object{
            private const val SHARED_PREF_NAME = "repo"
            private const val POSTS_KEY = "posts"
            private val gson = Gson()
            private val type =  TypeToken.getParameterized(List::class.java, Post::class.java).type
        }
}
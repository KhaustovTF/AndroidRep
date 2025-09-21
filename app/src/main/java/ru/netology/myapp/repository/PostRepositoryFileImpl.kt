package ru.netology.myapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.myapp.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {


    private var index: Long = 1L

    private var posts = emptyList<Post>()
        set(value) {
            field = value
            data.value = posts
            sync()
        }
    private val data = MutableLiveData(posts)


    init {
        val file = context.filesDir.resolve(FILE_NAME)

        if (file.exists()){
            file.bufferedReader().use { reader ->
                posts = gson.fromJson(reader, type)
                index = (posts.maxOfOrNull { post -> post.id } ?: 0) + 1
            }
        }else{
            sync()
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
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use { writer->
            writer.write(gson.toJson(posts, type))
        }
    }

        companion object{
            private const val FILE_NAME = "posts.json"
            private val gson = Gson()
            private val type =  TypeToken.getParameterized(List::class.java, Post::class.java).type
        }
}
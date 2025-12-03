package ru.netology.myapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.myapp.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositoryNetwork() : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()



    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        val jsonType = "application/json".toMediaType()
    }

    override fun get(): List<Post>{
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

         val call = client.newCall(request)

        val response = call.execute()
        val textBody = response.body.string()
        return gson.fromJson(textBody, type)
    }

    override fun like(id: Long) {
        val posts = get()
        val post = posts.find { it.id == id } ?: return

        val liked = post.likesByMe

        val url = "${BASE_URL}api/posts/$id/likes"

        val builder = Request.Builder()
            .url(url)

        val request = if (!liked) {
            builder
                .post("".toRequestBody(jsonType))
                .build()
        }else{
            builder
                .delete()
                .build()
        }

        val call = client.newCall(request)

        val response = call.execute()
        if (!response.isSuccessful) {
            response.close()
            throw RuntimeException("Oshibka")
        }

        val textBody = response.body.string()
        response.close()
    }

    override fun repost(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        val call = client.newCall(request)

        val response = call.execute()
        val textBody = response.body.string()
        return gson.fromJson(textBody, Post::class.java)
    }

    override fun editCancel(post: Post) {
        TODO("Not yet implemented")
    }


}

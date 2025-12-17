package ru.netology.myapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.myapp.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryNetwork : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        val jsonType = "application/json".toMediaType()
    }

    override fun get(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw RuntimeException("body is null")
        response.close()
        return gson.fromJson(body, type)
    }

    override fun like(post: Post): Post {
        val url = "${BASE_URL}api/posts/${post.id}/likes"
        val builder = Request.Builder().url(url)

        val request = if (!post.likesByMe) {
            builder.post("".toRequestBody(jsonType)).build()
        } else {
            builder.delete().build()
        }

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            response.close()
            throw RuntimeException("HTTP ${response.code}")
        }

        val body = response.body?.string() ?: throw RuntimeException("body is null")
        response.close()
        return gson.fromJson(body, Post::class.java)
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw RuntimeException("body is null")
        response.close()
        return gson.fromJson(body, Post::class.java)
    }

    override fun repost(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun editCancel(post: Post) {
        TODO("Not yet implemented")
    }


    override fun getAllAsync(callback: PostRepository.GetaAllCallback) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw RuntimeException("HTTP ${response.code}")
                    val postsJson = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(gson.fromJson(postsJson, type))
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }
        })
    }

    override fun likeAsync(post: Post, callback: PostRepository.LikeCallback) {
        val url = "${BASE_URL}api/posts/${post.id}/likes"
        val builder = Request.Builder().url(url)

        val request = if (!post.likesByMe) {
            builder.post("".toRequestBody(jsonType)).build()
        } else {
            builder.delete().build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw RuntimeException("HTTP ${response.code}")
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(gson.fromJson(body, Post::class.java))
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.SaveCallback) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw RuntimeException("HTTP ${response.code}")
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(gson.fromJson(body, Post::class.java))
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }
        })
    }
}

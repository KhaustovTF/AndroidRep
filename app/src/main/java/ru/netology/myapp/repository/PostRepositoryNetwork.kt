package ru.netology.myapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.myapp.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryNetwork : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val listType = TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback.onError(e)

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw RuntimeException("HTTP ${response.code}")
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(gson.fromJson(body, listType))
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }
        })
    }

    override fun likeById(id: Long, likedByMe: Boolean, callback: PostRepository.PostCallback<Post>) {
        val url = "${BASE_URL}api/posts/$id/likes"

        val builder = Request.Builder().url(url)

        val request = if (!likedByMe) {
            builder.post("".toRequestBody(jsonType)).build()
        } else {
            builder.delete().build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback.onError(e)

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

    override fun save(post: Post, callback: PostRepository.PostCallback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback.onError(e)

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

    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id")
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback.onError(e)

            override fun onResponse(call: Call, response: Response) {
                try {
                    if (!response.isSuccessful) throw RuntimeException("HTTP ${response.code}")
                    callback.onSuccess(Unit)
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }
        })
    }


}

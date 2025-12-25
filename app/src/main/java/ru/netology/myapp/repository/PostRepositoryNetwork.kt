package ru.netology.myapp.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.myapp.api.PostApi
import ru.netology.myapp.dto.Post

class PostRepositoryNetwork(
    private val api: PostApi = PostApi.service
) : PostRepository {

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        api.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(errorText(response)))
                    return
                }
                callback.onSuccess(response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun likeById(
        id: Long,
        likedByMe: Boolean,
        callback: PostRepository.PostCallback<Post>
    ) {
        val call = if (likedByMe) api.unlikeById(id) else api.likeById(id)

        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(errorText(response)))
                    return
                }

                val body = response.body()
                if (body == null) {
                    callback.onError(RuntimeException("Response body is null"))
                    return
                }

                callback.onSuccess(body)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun save(post: Post, callback: PostRepository.PostCallback<Post>) {
        api.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(errorText(response)))
                    return
                }

                val body = response.body()
                if (body == null) {
                    callback.onError(RuntimeException("Response body is null"))
                    return
                }

                callback.onSuccess(body)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun removeById(id: Long, callback: PostRepository.PostCallback<Unit>) {
        api.deleteById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(errorText(response)))
                    return
                }
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    private fun <T> errorText(response: Response<T>): String {
        val body = try {
            response.errorBody()?.string()
        } catch (e: Exception) {
            null
        }

        val base = "HTTP ${response.code()} ${response.message()}"
        return if (body.isNullOrBlank()) base else "$base: $body"
    }
}

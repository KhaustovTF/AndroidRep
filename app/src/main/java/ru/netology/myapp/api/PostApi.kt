package ru.netology.myapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.myapp.BuildConfig
import ru.netology.myapp.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = BuildConfig.BASE_URL // "http://10.0.2.2:9999/" и обязательно со / на конце


private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PostApi {
    @GET("api/slow/posts")
    fun getAll(): Call<List<Post>>

    @POST("api/slow/posts")
    fun save(@Body post: Post): Call<Post>

    @DELETE("api/slow/posts/{id}")
    fun deleteById(@Path("id") id: Long): Call<Unit>

    @POST("api/posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("api/posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call<Post>


    companion object {
        val service: PostApi by lazy { retrofit.create() }
    }
}

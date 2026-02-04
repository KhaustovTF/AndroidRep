package ru.netology.myapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import retrofit2.HttpException
import ru.netology.myapp.api.PostApi
import ru.netology.myapp.dto.Post
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryNetwork @Inject constructor(
    private val api: PostApi
) : PostRepository {

    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { PostPagingSource(api) }
    ).flow

    override suspend fun getAll(): List<Post> {
        val response = api.getAll()
        if (!response.isSuccessful) throw HttpException(response)
        return response.body().orEmpty()
    }

    override suspend fun likeById(id: Long, likedByMe: Boolean): Post {
        val response = if (likedByMe) api.unlikeById(id) else api.likeById(id)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body() ?: throw IOException("Response body is null")
    }

    override suspend fun removeById(id: Long) {
        val response = api.deleteById(id)
        if (!response.isSuccessful) throw HttpException(response)
    }

    override suspend fun save(post: Post): Post {
        val response = api.save(post)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body() ?: throw IOException("Response body is null")
    }
}

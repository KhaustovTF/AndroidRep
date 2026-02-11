package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import ru.netology.myapp.api.PostApi
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.entity.toDto
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PostRepositoryNetwork @Inject constructor(
    private val api: PostApi,
    private val db: AppDb,
    private val postDao: PostDao,
    private val keyDao: PostRemoteKeyDao,
) : PostRepository {

    override val data = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = PostRemoteMediator(api, db, postDao, keyDao),
        pagingSourceFactory = { postDao.pagingSource() }
    ).flow.map { paging ->
        paging.map { it.toDto() }
    }

    override suspend fun getAll() = emptyList<ru.netology.myapp.dto.Post>() // уже не нужно в лекционном стиле

    override suspend fun likeById(id: Long, likedByMe: Boolean): ru.netology.myapp.dto.Post {
        val response = if (likedByMe) api.unlikeById(id) else api.likeById(id)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body() ?: throw IOException("Response body is null")
    }

    override suspend fun removeById(id: Long) {
        val response = api.deleteById(id)
        if (!response.isSuccessful) throw HttpException(response)
    }

    override suspend fun save(post: ru.netology.myapp.dto.Post): ru.netology.myapp.dto.Post {
        val response = api.save(post)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body() ?: throw IOException("Response body is null")
    }
}
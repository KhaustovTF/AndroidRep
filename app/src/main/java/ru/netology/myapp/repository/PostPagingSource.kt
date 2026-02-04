package ru.netology.myapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.myapp.api.PostApi
import ru.netology.myapp.dto.Post
import java.io.IOException

class PostPagingSource(
    private val api: PostApi,
) : PagingSource<Long, Post>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        return try {
            val key = params.key

            val response = if (key == null) {
                api.getLatest(params.loadSize)
            } else {
                api.getBefore(key, params.loadSize)
            }

            if (!response.isSuccessful) {
                throw HttpException(response)
            }

            val data = response.body().orEmpty()

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = data.lastOrNull()?.id,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id
        }
    }
}

package ru.netology.myapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.netology.myapp.api.PostApi
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostRemoteKeyDao
import ru.netology.myapp.db.AppDb
import ru.netology.myapp.entity.PostEntity
import ru.netology.myapp.entity.PostRemoteKeyEntity
import ru.netology.myapp.entity.PostRemoteKeyEntity.KeyType
import ru.netology.myapp.entity.toEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val api: PostApi,
    private val db: AppDb,
    private val postDao: PostDao,
    private val keyDao: PostRemoteKeyDao,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            val pageSize = state.config.pageSize
            
            val currentAfter = keyDao.key(KeyType.AFTER) ?: postDao.maxId()
            val currentBefore = keyDao.key(KeyType.BEFORE) ?: postDao.minId()

            val response = when (loadType) {
                LoadType.PREPEND -> {

                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.REFRESH -> {

                    if (currentAfter == null) {
                        api.getLatest(pageSize)
                    } else {
                        api.getAfter(currentAfter, pageSize)
                    }
                }

                LoadType.APPEND -> {

                    val key = currentBefore ?: return MediatorResult.Success(endOfPaginationReached = true)
                    api.getBefore(key, pageSize)
                }
            }

            if (!response.isSuccessful) throw HttpException(response)
            val body = response.body().orEmpty()
            val endReached = body.isEmpty()

            db.withTransaction {
                if (body.isNotEmpty()) {
                    postDao.insert(body.toEntity())

                    val firstId = body.first().id
                    val lastId = body.last().id

                    when (loadType) {
                        LoadType.REFRESH -> {

                            keyDao.insert(PostRemoteKeyEntity(type = KeyType.AFTER, key = firstId))


                            if (currentBefore == null) {
                                keyDao.insert(PostRemoteKeyEntity(type = KeyType.BEFORE, key = lastId))
                            }
                        }

                        LoadType.APPEND -> {

                            keyDao.insert(PostRemoteKeyEntity(type = KeyType.BEFORE, key = lastId))

                            if (currentAfter == null) {
                                keyDao.insert(PostRemoteKeyEntity(type = KeyType.AFTER, key = firstId))
                            }
                        }

                        LoadType.PREPEND -> Unit
                    }
                } else {
                }
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
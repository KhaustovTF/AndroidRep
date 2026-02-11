package ru.netology.myapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.myapp.entity.PostRemoteKeyEntity
import ru.netology.myapp.entity.PostRemoteKeyEntity.KeyType

@Dao
interface PostRemoteKeyDao {

    @Query("SELECT `key` FROM PostRemoteKeyEntity WHERE type = :type")
    suspend fun key(type: KeyType): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<PostRemoteKeyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: PostRemoteKeyEntity)

    @Query("DELETE FROM PostRemoteKeyEntity")
    suspend fun clear()
}
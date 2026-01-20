package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity WHERE visible = 1 ORDER BY id DESC")
    fun getVisible(): Flow<List<PostEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT COUNT(*) FROM PostEntity")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM PostEntity WHERE visible = 0")
    fun hiddenCount(): Flow<Int>

    @Query("UPDATE PostEntity SET visible = 1 WHERE visible = 0")
    suspend fun showAllHidden()

    @Query("SELECT MAX(id) FROM PostEntity")
    suspend fun maxId(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)
}

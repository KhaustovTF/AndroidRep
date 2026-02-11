package ru.netology.myapp.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.myapp.db.PostColumns
import ru.netology.myapp.dto.Post

class LegacyPostDaoImpl(private val db: SQLiteDatabase) {

    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
                ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
                ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostColumns.COLUMN_LIKES_COUNT} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_LIKES_BY_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_REPOST_COUNT} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_VIDEO} TEXT
            )
        """.trimIndent()
    }

    fun getAll(): List<Post> {
        val result = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null, null, null, null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use { c ->
            while (c.moveToNext()) {
                result += c.mapToPost()
            }
        }
        return result
    }

    fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) put(PostColumns.COLUMN_ID, post.id)
            put(PostColumns.COLUMN_AUTHOR, if (post.id == 0L) "Me" else post.author)
            put(PostColumns.COLUMN_PUBLISHED, if (post.id == 0L) "now" else post.published)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_LIKES_COUNT, post.likesCount)
            put(PostColumns.COLUMN_LIKES_BY_ME, if (post.likesByMe) 1 else 0)
            put(PostColumns.COLUMN_REPOST_COUNT, post.repostCount)
            put(PostColumns.COLUMN_VIDEO, post.video)
        }

        val id = db.replace(PostColumns.TABLE, null, values)

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        ).use { c ->
            c.moveToNext()
            return c.mapToPost()
        }
    }

    fun likeById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostColumns.TABLE}
            SET ${PostColumns.COLUMN_LIKES_COUNT} = ${PostColumns.COLUMN_LIKES_COUNT} +
                CASE WHEN ${PostColumns.COLUMN_LIKES_BY_ME} THEN -1 ELSE 1 END,
                ${PostColumns.COLUMN_LIKES_BY_ME} = CASE
                    WHEN ${PostColumns.COLUMN_LIKES_BY_ME} THEN 0 ELSE 1
                END
            WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    fun repostById(id: Long) {
        db.execSQL(
            """
            UPDATE ${PostColumns.TABLE}
            SET ${PostColumns.COLUMN_REPOST_COUNT} = ${PostColumns.COLUMN_REPOST_COUNT} + 1
            WHERE ${PostColumns.COLUMN_ID} = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun Cursor.mapToPost(): Post = Post(
        id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
        author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
        published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
        content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
        likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNT)),
        likesByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_BY_ME)) != 0,
        repostCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_REPOST_COUNT)),
        video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
    )
}
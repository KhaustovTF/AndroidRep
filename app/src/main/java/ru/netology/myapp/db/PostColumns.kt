package ru.netology.myapp.db

object PostColumns {
    const val TABLE = "posts"

    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_PUBLISHED = "published"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_LIKES_COUNT = "likesCount"
    const val COLUMN_LIKES_BY_ME = "likesByMe"
    const val COLUMN_REPOST_COUNT = "repostCount"
    const val COLUMN_VIDEO = "video"

    val ALL_COLUMNS = arrayOf(
        COLUMN_ID,
        COLUMN_AUTHOR,
        COLUMN_PUBLISHED,
        COLUMN_CONTENT,
        COLUMN_LIKES_COUNT,
        COLUMN_LIKES_BY_ME,
        COLUMN_REPOST_COUNT,
        COLUMN_VIDEO
    )
}
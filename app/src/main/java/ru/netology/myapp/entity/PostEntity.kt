package ru.netology.myapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.myapp.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val video: String?,
    val authorAvatar: String?,
    val likesCount: Int,
    val likesByMe: Boolean,
    val repostCount: Int,
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        video = video,
        authorAvatar = authorAvatar,
        likesCount = likesCount,
        likesByMe = likesByMe,
        repostCount = repostCount,
    )

    companion object {
        fun fromDto(dto: Post): PostEntity = PostEntity(
            id = dto.id,
            author = dto.author,
            published = dto.published,
            content = dto.content,
            video = dto.video,
            authorAvatar = dto.authorAvatar,
            likesCount = dto.likesCount,
            likesByMe = dto.likesByMe,
            repostCount = dto.repostCount,
        )
    }
}

fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
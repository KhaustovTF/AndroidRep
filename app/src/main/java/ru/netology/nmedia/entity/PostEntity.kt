package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val visible: Boolean = true,
) {
    fun toDto() = Post(
        id = id,
        content = content,
        author = author,
        authorAvatar = authorAvatar,
        likedByMe = likedByMe,
        likes = likes,
        published = published,
    )

    companion object {
        fun fromDto(dto: Post, visible: Boolean = true) =
            PostEntity(
                id = dto.id,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                content = dto.content,
                published = dto.published,
                likedByMe = dto.likedByMe,
                likes = dto.likes,
                visible = visible,
            )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)

fun List<Post>.toEntity(visible: Boolean = true): List<PostEntity> =
    map { PostEntity.fromDto(it, visible) }

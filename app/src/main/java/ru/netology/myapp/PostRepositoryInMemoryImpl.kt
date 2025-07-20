package ru.netology.myapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post

class PostRepositoryInMemoryImpl: PostRepository {


     private var post = Post(
        id = 1,
        author = "Нетология. Университет инернет-профессий будущего",
        published = "21 мая в 18:36",
        content = "Сегодня справился с очередной порцией заданий — и настроение сразу поднялось. Друзья, делитесь своими лайфхаками: как вам удается поддерживать мотивацию и эффективно учиться? #учёба #домашнеезадание #мотивация #студенты #школьники"
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount++)
        data.value = post

    }

    override fun repost() {
        post.copy(repostByMe = !post.repostByMe, repostCount = post.repostCount++)
        data.value = post
    }
}
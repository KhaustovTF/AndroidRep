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
        if (post.likesByMe==false){
            post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount+1)
            data.value = post
        }else{
            post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount-1)
            data.value = post
        }



//        data.value = post


    }

    override fun repost() {
        post = post.copy(repostByMe = !post.repostByMe, repostCount = post.repostCount+1)

        data.value = post
    }
}
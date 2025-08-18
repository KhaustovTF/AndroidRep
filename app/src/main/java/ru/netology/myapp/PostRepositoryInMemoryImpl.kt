package ru.netology.myapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.myapp.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {


    private var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет инернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Сегодня справился с очередной порцией заданий — и настроение сразу поднялось. Друзья, делитесь своими лайфхаками: как вам удается поддерживать мотивацию и эффективно учиться? #учёба #домашнеезадание #мотивация #студенты #школьники"
        ),
        Post(
            id = 2,
            author = "Нетология. Университет инернет-профессий прошлого",
            published = "22 мая в 19:37",
            content = "тут могла быть ваша реклама"
        ),
        Post(
            id = 3,
            author = "DOTA 2 FOREVER + MALCHISHNIK v ETU SATURDAY!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
            published = "16 avgusta в 19:00 - 20:00",
            content = "BUHAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEM"
        ),
        Post(
            id = 4,
            author = "Нетология. Университет инернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Сегодня справился с очередной порцией заданий — и настроение сразу поднялось. Друзья, делитесь своими лайфхаками: как вам удается поддерживать мотивацию и эффективно учиться? #учёба #домашнеезадание #мотивация #студенты #школьники"
        ),
        Post(
            id = 5,
            author = "Нетология. Университет инернет-профессий прошлого",
            published = "22 мая в 19:37",
            content = "тут могла быть ваша реклама"
        ),
        Post(
            id = 6,
            author = "DOTA 2 FOREVER + MALCHISHNIK v ETU SATURDAY!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
            published = "16 avgusta в 19:00 - 20:00",
            content = "BUHAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEM"
        ),
        Post(
            id = 1,
            author = "Нетология. Университет инернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Сегодня справился с очередной порцией заданий — и настроение сразу поднялось. Друзья, делитесь своими лайфхаками: как вам удается поддерживать мотивацию и эффективно учиться? #учёба #домашнеезадание #мотивация #студенты #школьники"
        ),
        Post(
            id = 2,
            author = "Нетология. Университет инернет-профессий прошлого",
            published = "22 мая в 19:37",
            content = "тут могла быть ваша реклама"
        ),
        Post(
            id = 3,
            author = "DOTA 2 FOREVER + MALCHISHNIK v ETU SATURDAY!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
            published = "16 avgusta в 19:00 - 20:00",
            content = "BUHAEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEM"
        )
    )

    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likesByMe = !post.likesByMe,
                    likesCount = if (post.likesByMe) post.likesCount - 1 else post.likesCount + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    //            if (posts.likesByMe == false) {
//                post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount + 1)
//                data.value = post
//            } else {
//                post = post.copy(likesByMe = !post.likesByMe, likesCount = post.likesCount - 1)
//                data.value = post
//            }


    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    repostByMe = !post.repostByMe, repostCount = post.repostCount + 1
                )
            } else {
                post
            }
        }
        data.value = posts

    }
}
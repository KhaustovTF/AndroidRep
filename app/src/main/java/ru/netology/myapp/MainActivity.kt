package ru.netology.myapp


import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет инернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Сегодня справился с очередной порцией заданий — и настроение сразу поднялось. Друзья, делитесь своими лайфхаками: как вам удается поддерживать мотивацию и эффективно учиться? #учёба #домашнеезадание #мотивация #студенты #школьники"
        )

        with(binding) {
            authorNameText.text = post.author
            postsContent.text = post.content
            publishedTimeText.text = post.published
            likeButtonCount.text = post.likesCount.toString()
            repostButtonCount.text = post.repostCount.toString()

//            like button code

            if (post.likesByMe){
                likeButton.setImageResource(R.drawable.baseline_favorite_24)
            }

            likeButton.setOnClickListener{
                post.likesByMe = !post.likesByMe

                if (post.likesByMe){
                    post.likesCount++
                }else{
                    post.likesCount--
                }
                likeButtonCount.text = "${post.likesCount}"


                likeButton.setImageResource(
                    if (post.likesByMe) {
                        R.drawable.baseline_favorite_24

                    }else{
                        R.drawable.baseline_favorite_border_24

                    }
                )


            }

//            Share button code

            repostButton.setOnClickListener {
                post.repostCount++
                repostButtonCount.text = "${post.repostCount}"
            }
        }




    }
}
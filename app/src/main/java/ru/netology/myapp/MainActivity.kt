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
            likeButtonCount.text = countefixer(post.likesCount)
            repostButtonCount.text = countefixer(post.repostCount)

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
                likeButtonCount.text = countefixer(post.likesCount)


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
                repostButtonCount.text = countefixer(post.repostCount)
            }
        }




    }
}

fun countefixer(number: Int): String {
        return when {
            number < 1000 -> number.toString()
            number < 10000 -> {
                val thousands = number / 1000
                val remainder = number % 1000
                val hundreds = remainder / 100
                if (hundreds == 0) "${thousands}K" else "${thousands}.${hundreds}K"
            }
            number < 1000000 -> {
                "${number / 1000}K"
            }
            number < 10000000 -> {
                val millions = number / 1000000
                val remainder = number % 1000000
                val hundredsOfThousands = remainder / 100000
                if (hundredsOfThousands == 0) "${millions}M" else "${millions}.${hundredsOfThousands}M"
            }
            else -> {
                val millions = number / 1000000
                "${millions}M"
            }
        }
    }

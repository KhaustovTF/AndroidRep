package ru.netology.myapp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        viewModel.data.observe(this) { post ->

            with(binding) {
                authorNameText.text = post.author
                postsContent.text = post.content
                publishedTimeText.text = post.published
                likeButtonCount.text = post.likesCount.toString()
                repostButtonCount.text = post.repostCount.toString()

//            like button code

                likeButton.setImageResource(
                    if (post.likesByMe) {
                        R.drawable.baseline_favorite_24

                    } else {
                        R.drawable.baseline_favorite_border_24

                    }
                )
            }

            binding.likeButton.setOnClickListener {
                    viewModel.like()


//                if (post.likesByMe){
//                    post.likesCount++
//                }else{
//                    post.likesCount--
//                }


            }

//            Repost-button code

            binding.repostButton.setOnClickListener {
//                post.repostCount++
                viewModel.repost()
//                repostButtonCount.text = countefixer(post.repostCount)
            }
        }


    }
}



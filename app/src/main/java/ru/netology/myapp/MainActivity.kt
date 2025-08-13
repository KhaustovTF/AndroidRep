package ru.netology.myapp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        viewModel.data.observe(this) { posts ->
            binding.container.removeAllViews()
            posts.forEach { post ->
                CardPostBinding.inflate(layoutInflater, binding.container, true).apply {

                    authorNameText.text = post.author
                    postsContent.text = post.content
                    publishedTimeText.text = post.published
                    likeButtonCount.text = SingleCountFix.counteFixer(post.likesCount)
                    repostButtonCount.text = SingleCountFix.counteFixer(post.repostCount)

//            like button codee

                    likeButton.setImageResource(
                        if (post.likesByMe) {
                            R.drawable.baseline_favorite_24

                        } else {
                            R.drawable.baseline_favorite_border_24

                        }
                    )
                    likeButton.setOnClickListener {
                        viewModel.like(post.id)
                    }

                    repostButton.setOnClickListener {
//                post.repostCount++
                        viewModel.repost(post.id)
//                repostButtonCount.text = countefixer(post.repostCount)
                    }

                }.root

            }


//                if (post.likesByMe){
//                    post.likesCount++
//                }else{
//                    post.likesCount--
//                }



        }

//            Repost-button code


        }


    }




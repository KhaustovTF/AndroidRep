package ru.netology.myapp


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.adapter.PostAdapter
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.databinding.CardPostBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        val adapter = PostAdapter { post ->
            viewModel.like(post.id)
            viewModel.repost(post.id)
        }

        binding.list.adapter = adapter


        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

    }


}







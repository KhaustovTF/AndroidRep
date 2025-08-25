package ru.netology.myapp


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostAdapter
import ru.netology.myapp.adapter.onRemoveListener
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.util.AndroidUtils


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

        }
        )
        binding.list.adapter = adapter


        viewModel.data.observe(this) { posts ->
            val isNew = posts.size != adapter.itemCount
            adapter.submitList(posts) {
                if (isNew) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                with(binding.content) {
                    requestFocus()
                    setText(post.content)
                }
            }
        }


        with(binding) {
            binding.save.setOnClickListener {
                if (content.text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Text vvedi ",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContet(content.text.toString())
                viewModel.save()
                content.setText("")
                content.clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
        }


    }


}







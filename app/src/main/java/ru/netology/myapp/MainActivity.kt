package ru.netology.myapp


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostAdapter
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.util.AndroidUtils


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()


        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { content ->
            content ?: return@registerForActivityResult
            viewModel.changeContent(content)
            viewModel.save()
        }

        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { content ->
            if (content == null) {
                viewModel.editCancel()
                return@registerForActivityResult
            }
            viewModel.changeContent(content)
            viewModel.save()
        }

        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                startActivity(intent)
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }

            override fun onPlayVideo(post: Post) {
                post.video?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    startActivity(intent)
                }
            }

        }
        )
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val isNew = posts.size > adapter.itemCount
            adapter.submitList(posts) {
                if (isNew) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }




        binding.fab.setOnClickListener {
            newPostLauncher.launch(Unit)
        }








//        viewModel.edited.observe(this) { post ->
//            if (post.id != 0L) {
//                with(binding.content) {
//                    requestFocus()
//                    setText(post.content)
//                    binding.editGroup.visibility = View.VISIBLE
//                }
//            }
//        }
//
//
//        with(binding) {
//            save.setOnClickListener {
//                if (content.text.isNullOrBlank()) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Text vvedi ",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    return@setOnClickListener
//                }
//
//                viewModel.changeContent(content.text.toString())
//                viewModel.save()
//                content.setText("")
//                content.clearFocus()
//                editGroup.visibility = View.GONE
//                AndroidUtils.hideKeyboard(it)
//
//            }
//
//            cancelEdit.setOnClickListener {
//
//                viewModel.editCancel()
//                viewModel.save()
//                content.setText("")
//                editGroup.visibility = View.GONE
//                AndroidUtils.hideKeyboard(it)
//            }
//
//        }


    }


}







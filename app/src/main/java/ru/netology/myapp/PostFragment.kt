package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.FeedFragment.Companion.textArgs
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostViewHolder
import ru.netology.myapp.databinding.FragmentSinglePostBinding
import ru.netology.myapp.dto.Post

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val postId: Long by lazy { requireArguments().getLong("postId") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePostBinding.inflate(inflater, container, false)

        val holder = PostViewHolder(binding.singlePost, object : OnInteractorListener {

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                startActivity(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                )
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.newPostFragment,
                    Bundle().apply { textArgs = post.content }
                )
            }

            override fun onPlayVideo(post: Post) {
                post.video?.let { url ->
                    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                }
            }

            // onOpen не нужен — оставляем дефолтный пустой
        })

        viewModel.data.observe(viewLifecycleOwner) { state ->
            val post = state.post.find { it.id == postId } ?: return@observe
            holder.bind(post)
        }

        return binding.root
    }
}

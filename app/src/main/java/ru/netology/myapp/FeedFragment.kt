package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostAdapter
import ru.netology.myapp.databinding.FragmentFeedBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.util.StringArg

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.like(post)
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
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment2_to_newPostFragment,
                    Bundle().apply { textArgs = post.content }
                )
            }

            override fun onPlayVideo(post: Post) {
                post.video?.let { url ->
                    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                }
            }

            override fun onOpen(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment2_to_postFragment,
                    Bundle().apply { putLong("postId", post.id) }
                )
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner){ state ->
            adapter.submitList(state.post)
            binding.progress.isVisible = state.loading
            binding.empty.isVisible = state.empty
            binding.errorGroup.isVisible = state.error

        }
        binding.retry.setOnClickListener{
            viewModel.load()
        }



        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment2_to_newPostFragment)
        }

        return binding.root
    }

    companion object {
        var Bundle.textArgs by StringArg
    }
}

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
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.myapp.FeedFragment.Companion.textArgs
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostAdapter
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

        val adapter = PostAdapter(object : OnInteractorListener {
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
                requireActivity().onBackPressedDispatcher.onBackPressed()
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

            override fun onOpen(post: Post) {

            }
        })

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId }
            post?.let { adapter.submitList(listOf(it)) }
        }

        return binding.root
    }
}

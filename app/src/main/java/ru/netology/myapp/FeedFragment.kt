package ru.netology.myapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.myapp.adapter.OnInteractorListener
import ru.netology.myapp.adapter.PostAdapter
import ru.netology.myapp.auth.AuthViewModel
import ru.netology.myapp.databinding.FragmentFeedBinding
import ru.netology.myapp.dto.Post
import ru.netology.myapp.util.StringArg

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(inflater, container, false)

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest { state ->
                    val refresh = state.refresh
                    binding.progress.isVisible = refresh is LoadState.Loading
                    binding.errorGroup.isVisible = refresh is LoadState.Error
                    binding.empty.isVisible =
                        refresh is LoadState.NotLoading && adapter.itemCount == 0
                }
            }
        }

        binding.retry.setOnClickListener {
            adapter.retry()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment2_to_newPostFragment)
        }

        viewModel.refresh.observe(viewLifecycleOwner) {
            adapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authorized.collectLatest {
                    adapter.refresh()
                }
            }
        }

        binding.empty.setOnClickListener {
            authViewModel.toggle()
        }

        return binding.root
    }

    companion object {
        var Bundle.textArgs by StringArg
    }
}

package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.databinding.ItemLoadingBinding

class PostsLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PostsLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding, retry)
    }

    class LoadStateViewHolder(
        private val binding: ItemLoadingBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            binding.progress.isVisible = loadState is LoadState.Loading
            binding.retry.isVisible = loadState is LoadState.Error
        }
    }
}
package ru.netology.myapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.SingleCountFix
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post
import androidx.core.net.toUri
import ru.netology.myapp.adapter.PostViewHolder.PostDiffCallBack

typealias onLikeListener = (post: Post) -> Unit
typealias onRepostListener = (post: Post) -> Unit
typealias onRemoveListener = (post: Post) -> Unit

interface OnInteractorListener {
    fun onLike(post: Post)
    fun onRepost(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlayVideo(post: Post)
    fun onOpen(post: Post) {}
}

class PostAdapter(
    private val onInteractorListener: OnInteractorListener,
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallBack) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractorListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }


}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractorListener: OnInteractorListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        authorNameText.text = post.author
        postsContent.text = post.content
        publishedTimeText.text = post.published

//            like button codee
        likeButton.apply {
            isChecked = post.likesByMe
            text = post.likesCount.toString()
            text = SingleCountFix.counteFixer(post.likesCount)
        }
        repostButton.apply {
            text = post.repostCount.toString()
            text = SingleCountFix.counteFixer(post.repostCount)
        }


        likeButton.setOnClickListener {
            onInteractorListener.onLike(post)
        }

        repostButton.setOnClickListener {
//                post.repostCount++
            onInteractorListener.onRepost(post)
//                repostButtonCount.text = countefixer(post.repostCount)
        }

        moreVertButton.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_options)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.remove -> {
                            onInteractorListener.onRemove(post)
                            true
                        }

                        R.id.edit_post -> {
                            onInteractorListener.onEdit(post)
                            true
                        }

                        else -> false
                    }
                }


            }.show()
        }

        binding.root.setOnClickListener {
            onInteractorListener.onOpen(post)
        }

        if (post.video.isNullOrBlank()) {
            videoPlace.visibility = View.GONE
        } else {
            videoPlace.visibility = View.VISIBLE

            val clickListener = View.OnClickListener {
                onInteractorListener.onPlayVideo(post)
            }

            videoPlace.setOnClickListener(clickListener)
            playButton.setOnClickListener(clickListener)
        }
        binding.root.setOnClickListener {
            onInteractorListener.onOpen(post)
        }
        binding.root.isClickable = true
        binding.root.isFocusable = true

    }

    object PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}


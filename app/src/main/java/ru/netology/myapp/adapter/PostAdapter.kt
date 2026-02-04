package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.myapp.R
import ru.netology.myapp.SingleCountFix
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post

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
) : PagingDataAdapter<Post, PostViewHolder>(PostDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractorListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return
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

        val avatarUrl = post.authorAvatar?.let { "http://10.0.2.2:9999/avatars/$it" }

        Glide.with(postsAvatar)
            .load(avatarUrl)
            .timeout(10_000)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .circleCrop()
            .into(postsAvatar)

        likeButton.apply {
            isChecked = post.likesByMe
            text = SingleCountFix.counteFixer(post.likesCount)
        }

        repostButton.apply {
            text = SingleCountFix.counteFixer(post.repostCount)
        }

        likeButton.setOnClickListener {
            onInteractorListener.onLike(post)
        }

        repostButton.setOnClickListener {
            onInteractorListener.onRepost(post)
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
    }
}

object PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}

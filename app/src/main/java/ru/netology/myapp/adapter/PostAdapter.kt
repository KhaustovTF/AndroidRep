package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.SingleCountFix
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post

typealias onLikeListener = (post: Post) -> Unit
typealias onRepostListener = (post: Post) -> Unit
typealias onRemoveListener = (post: Post) -> Unit

interface OnInteractorListener {
    fun onLike(post: Post)
    fun onRepost(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
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
        likeButtonCount.text = SingleCountFix.counteFixer(post.likesCount)
        repostButtonCount.text = SingleCountFix.counteFixer(post.repostCount)

//            like button codee

        likeButton.setImageResource(
            if (post.likesByMe) {
                R.drawable.baseline_favorite_24

            } else {
                R.drawable.baseline_favorite_border_24

            }
        )
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

                        R.id.edit -> {
                            onInteractorListener.onEdit(post)
                            true
                        }

                        else -> false
                    }
                }


            }.show()
        }
    }
}

object PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}


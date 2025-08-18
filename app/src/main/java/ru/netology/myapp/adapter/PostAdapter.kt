package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.R
import ru.netology.myapp.SingleCountFix
import ru.netology.myapp.databinding.CardPostBinding
import ru.netology.myapp.dto.Post

typealias onLikeListener = (post: Post) -> Unit
typealias onRepostListener = (post: Post) -> Unit


class PostAdapter(private val onLikeListener: onLikeListener, private val onRepostListener: onRepostListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallBack) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener,onRepostListener)
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
    private val onLikeListener: onLikeListener,
    private val onRepostListener: onRepostListener
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
            onLikeListener(post)
        }

        repostButton.setOnClickListener {
//                post.repostCount++
            onRepostListener(post)
//                repostButtonCount.text = countefixer(post.repostCount)
        }
    }
}

object PostDiffCallBack: DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}


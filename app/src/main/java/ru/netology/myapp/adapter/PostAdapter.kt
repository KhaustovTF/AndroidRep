package ru.netology.myapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myapp.databinding.ActivityMainBinding
import ru.netology.myapp.dto.Post

typealias onLikeListener = (post: Post) -> Unit


class PostAdapter(private val onLikeListener: onLikeListener) :
    RecyclerView.Adapter<PostViewHolder>() {

    var list: List<Post> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return PostViewHolder(binding, onLikeListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = list[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = list.size

}

class PostViewHolder(
    private val binding: ActivityMainBinding,
    private val onLikeListener: onLikeListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {

    }
}
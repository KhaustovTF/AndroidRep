package ru.netology.myapp

import androidx.lifecycle.LiveData
import ru.netology.myapp.dto.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun repost()

    fun countefixer(number: Int): String {
        return when {
            number < 1000 -> number.toString()
            number < 10000 -> {
                val thousands = number / 1000
                val remainder = number % 1000
                val hundreds = remainder / 100
                if (hundreds == 0) "${thousands}K" else "${thousands}.${hundreds}K"
            }

            number < 1000000 -> {
                "${number / 1000}K"
            }

            number < 10000000 -> {
                val millions = number / 1000000
                val remainder = number % 1000000
                val hundredsOfThousands = remainder / 100000
                if (hundredsOfThousands == 0) "${millions}M" else "${millions}.${hundredsOfThousands}M"
            }

            else -> {
                val millions = number / 1000000
                "${millions}M"
            }
        }
    }
}
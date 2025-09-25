package ru.netology.myapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.myapp.dao.PostDao
import ru.netology.myapp.dao.PostDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(buildDatabase(context)).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): SQLiteDatabase {
            val helper = DbHelper(
                context = context,
                dbName = "app.db",
                ddls = arrayOf(PostDaoImpl.DDL),
                dbVersion = 1
            )
            return helper.writableDatabase
        }
    }
}

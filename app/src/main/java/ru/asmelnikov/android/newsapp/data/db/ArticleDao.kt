package ru.asmelnikov.android.newsapp.data.db

import androidx.room.*
import ru.asmelnikov.android.newsapp.models.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAllArticles(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)
}
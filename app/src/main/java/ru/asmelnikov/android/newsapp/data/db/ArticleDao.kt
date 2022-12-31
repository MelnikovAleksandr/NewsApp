package ru.asmelnikov.android.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.asmelnikov.android.newsapp.models.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAllArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT COUNT(id) FROM article")
    fun getCount(): LiveData<Int>
}
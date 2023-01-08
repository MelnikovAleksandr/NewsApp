package ru.asmelnikov.android.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "article")
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    @PrimaryKey(autoGenerate = false)
    val publishedAt: String,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
) : Serializable
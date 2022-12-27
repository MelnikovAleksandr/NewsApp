package ru.asmelnikov.android.newsapp.models

data class NewsReppons(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
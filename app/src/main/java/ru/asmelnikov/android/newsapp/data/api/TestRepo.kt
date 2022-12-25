package ru.asmelnikov.android.newsapp.data.api

import javax.inject.Inject

class TestRepo @Inject constructor(private val newsService: NewsService) {

    suspend fun getAll() = newsService.getHeadlines()

}
package ru.asmelnikov.android.newsapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.asmelnikov.android.newsapp.models.NewsReppons

private const val API_KEY = "30902f38d91440588b9c1a0b43035e83"

interface NewsService {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsReppons>

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") countryCode: String = "",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsReppons>
}
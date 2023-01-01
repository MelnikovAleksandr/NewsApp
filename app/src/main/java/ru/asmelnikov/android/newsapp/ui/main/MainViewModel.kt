package ru.asmelnikov.android.newsapp.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.models.NewsReppons
import ru.asmelnikov.android.newsapp.utils.Resource
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val newsLiveData: MutableLiveData<Resource<NewsReppons>> =
        MutableLiveData()

    private var newsPage = 1

    private var regionOfPopularNews: String =
        context.resources.getString(ru.asmelnikov.android.newsapp.R.string.region_popular_news)

    init {
        getNews(regionOfPopularNews)
    }

    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resource.Loading())
            val response = repository.getNews(
                countryCode = countryCode,
                pageNumber = newsPage
            )
            if (response.isSuccessful) {
                response.body().let { res ->
                    newsLiveData.postValue(Resource.Success(res))
                }
            } else {
                newsLiveData.postValue(Resource.Error(message = response.message()))
            }
        }

}
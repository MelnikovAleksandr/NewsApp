package ru.asmelnikov.android.newsapp.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.di.NewsApp
import ru.asmelnikov.android.newsapp.models.NewsReppons
import ru.asmelnikov.android.newsapp.utils.Resource
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app: Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {

    val newsLiveData: MutableLiveData<Resource<NewsReppons>> =
        MutableLiveData()

    private val newsPage = 1

    private val regionOfPopularNews =
        app.resources.getString(R.string.region_popular_news)
    private val errorMessage =
        app.resources.getString(R.string.no_internet)


    init {
        getNews(regionOfPopularNews)
    }

    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            safeGetNewsCall(countryCode)
        }

    private suspend fun safeGetNewsCall(countryCode: String) {
        newsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getNews(
                    countryCode = countryCode,
                    pageNumber = newsPage
                )
                response.body().let { res ->
                    newsLiveData.postValue(Resource.Success(res))
                }
            } else {
                newsLiveData.postValue(Resource.Error(errorMessage))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsLiveData.postValue(Resource.Error(errorMessage))
                else -> newsLiveData.postValue(Resource.Error(errorMessage))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApp>()
            .getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(
            activeNetwork
        ) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
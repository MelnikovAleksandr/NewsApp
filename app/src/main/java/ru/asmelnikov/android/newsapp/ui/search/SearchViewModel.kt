package ru.asmelnikov.android.newsapp.ui.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
class SearchViewModel @Inject constructor(
    app: Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {

    val searchNewsLiveData: MutableLiveData<Resource<NewsReppons>> = MutableLiveData()
    private val searchNewsPage = 1

    private val errorMessage =
        app.resources.getString(R.string.no_internet)

    init {
        getSearchNews(query = "")
    }

    fun getSearchNews(query: String) =
        viewModelScope.launch {
            safeGetSearchCall(query)
        }

    private suspend fun safeGetSearchCall(query: String) {
        searchNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getSearchNews(
                    query = query, pageNumber = searchNewsPage
                )
                response.body().let { res ->
                    searchNewsLiveData.postValue(Resource.Success(res))
                }
            } else {
                searchNewsLiveData.postValue(Resource.Error(errorMessage))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNewsLiveData.postValue(Resource.Error(errorMessage))
                else -> searchNewsLiveData.postValue(Resource.Error(errorMessage))
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
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
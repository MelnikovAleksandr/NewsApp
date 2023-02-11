package ru.asmelnikov.android.newsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.models.NewsReppons
import ru.asmelnikov.android.newsapp.ui.details.Event
import ru.asmelnikov.android.newsapp.utils.Resource
import ru.asmelnikov.android.newsapp.utils.ResourceProvider
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
    mResourceProvider: ResourceProvider,
) : ViewModel() {

    private val _newsLideData = MutableLiveData<Resource<NewsReppons>>()

    val newsLiveData: LiveData<Resource<NewsReppons>>
        get() = _newsLideData

    private val newsPage = 1

    init {
        getNews(mResourceProvider.getString(R.string.region_popular_news))
    }

    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            _newsLideData.postValue(Resource.Loading())
            val response = repository.getNews(
                countryCode = countryCode,
                pageNumber = newsPage
            )
            if (response.isSuccessful) {
                response.body().let { res ->
                    _newsLideData.postValue(Resource.Success(res))
                }
            } else {
                _newsLideData.postValue(Resource.Error(message = response.message()))
            }
        }
}
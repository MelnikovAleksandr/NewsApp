package ru.asmelnikov.android.newsapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.models.Article
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val isFavorite by lazy {
        MutableLiveData<Int>(0)
    }

    fun find(article: Article) {
        viewModelScope.launch {
            isFavorite.value = repository.find(article)
        }
    }

    fun favoriteCheck(article: Article) {
        viewModelScope.launch {
            if (isFavorite.value == 0) {
                repository.addToFavorite(article)
            } else {
                repository.deleteFromFavorite(article)
            }
            find(article)
        }
    }
}
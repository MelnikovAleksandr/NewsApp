package ru.asmelnikov.android.newsapp.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.R
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.models.Article
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: NewsRepository,
    app: Application
) : AndroidViewModel(app) {

    private val successAdd: String =
        app.resources.getString(R.string.successfully_add)

    private val successDelete: String =
        app.resources.getString(R.string.successfully_deleted)

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    val isFavorite by lazy {
        MutableLiveData<Int>(0)
    }

    fun favoritesCheck(article: Article) {
        viewModelScope.launch {
            isFavorite.value = repository.find(article)
        }
    }

    fun favoriteAddAndCheck(article: Article) {
        viewModelScope.launch {
            if (isFavorite.value == 0) {
                repository.addToFavorite(article)
                statusMessage.value = Event(successAdd)
            } else {
                repository.deleteFromFavorite(article)
                statusMessage.value = Event(successDelete)
            }
            favoritesCheck(article)
        }
    }
}
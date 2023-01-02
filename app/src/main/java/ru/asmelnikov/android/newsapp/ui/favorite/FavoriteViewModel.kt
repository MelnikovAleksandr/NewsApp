package ru.asmelnikov.android.newsapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import ru.asmelnikov.android.newsapp.models.Article
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    fun getAllFavorites() = repository.getFavoriteArticles()

    fun getCount(): LiveData<Int> {
        return repository.getCount()
    }

    fun saveFavoriteArticle(article: Article) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        repository.addToFavorite(article = article)
    }

    fun deleteFavoriteArticle(article: Article) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        repository.deleteFromFavorite(article = article)
    }
}

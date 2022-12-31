package ru.asmelnikov.android.newsapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.asmelnikov.android.newsapp.data.api.NewsRepository
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    fun getAllFavorites() = repository.getFavoriteArticles()

    fun getCount(): LiveData<Int> {
        return repository.getCount()
    }
}

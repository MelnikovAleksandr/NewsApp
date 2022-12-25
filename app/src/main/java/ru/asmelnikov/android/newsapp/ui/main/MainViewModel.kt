package ru.asmelnikov.android.newsapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.data.api.TestRepo
import ru.asmelnikov.android.newsapp.models.NewsReppons
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val testRepo: TestRepo) : ViewModel() {

    private val _all = MutableLiveData<NewsReppons>()
    val all: LiveData<NewsReppons>
        get() = _all

    init {
        getAll()
    }

    fun getAll() = viewModelScope.launch {
        testRepo.getAll().let {
            if (it.isSuccessful) {
                _all.postValue(it.body())
            } else {
                Log.d(
                    "checkData", "Failed to load articles: " +
                            "${it.errorBody()}"
                )
            }
        }
    }

}
package ru.asmelnikov.android.newsapp.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


abstract class ResourceInterface {

    abstract fun getString(id: Int): String
}

@Singleton
class ResourceProvider @Inject constructor(@ApplicationContext private val context: Context) :
    ResourceInterface() {

    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}




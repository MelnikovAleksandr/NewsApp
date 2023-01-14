package ru.asmelnikov.android.newsapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.asmelnikov.android.newsapp.R

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_image)
        .into(this)
}


package com.example.weatherforecastapp.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageUrl(url: String) {
    Glide.with(this.context).load(url)
        .into(this)
}
package com.example.weatherforecastapp.data.network.model.forecast

import com.google.gson.annotations.SerializedName

data class WindResponse(
    @SerializedName("deg")
    val deg: Int?,
    @SerializedName("gust")
    val gust: Double?,
    @SerializedName("speed")
    val speed: Double?
)
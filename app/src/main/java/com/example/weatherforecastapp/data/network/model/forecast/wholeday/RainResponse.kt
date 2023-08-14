package com.example.weatherforecastapp.data.network.model.forecast.wholeday

import com.google.gson.annotations.SerializedName

data class RainResponse(
    @SerializedName("3h")
    val h: Double?
)
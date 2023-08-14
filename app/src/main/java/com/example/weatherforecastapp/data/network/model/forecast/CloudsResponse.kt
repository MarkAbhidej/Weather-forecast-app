package com.example.weatherforecastapp.data.network.model.forecast

import com.google.gson.annotations.SerializedName

data class CloudsResponse(
    @SerializedName("all")
    val all: Int?
)
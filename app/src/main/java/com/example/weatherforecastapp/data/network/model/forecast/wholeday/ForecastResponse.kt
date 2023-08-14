package com.example.weatherforecastapp.data.network.model.forecast.wholeday

import com.example.weatherforecastapp.data.network.model.forecast.*
import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("clouds")
    val clouds: CloudsResponse?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("dt_txt")
    val dtTxt: String?,
    @SerializedName("main")
    val main: MainResponse?,
    @SerializedName("pop")
    val pop: Double?,
    @SerializedName("rain")
    val rain: RainResponse?,
    @SerializedName("sys")
    val sys: SysResponse?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<WeatherResponse>?,
    @SerializedName("wind")
    val wind: WindResponse?
)
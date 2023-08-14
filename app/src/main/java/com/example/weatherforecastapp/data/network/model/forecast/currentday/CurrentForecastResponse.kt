package com.example.weatherforecastapp.data.network.model.forecast.currentday

import com.example.weatherforecastapp.data.network.model.forecast.*
import com.example.weatherforecastapp.domain.model.*
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.google.gson.annotations.SerializedName

data class CurrentForecastResponse(
    @SerializedName("base")
    val base: String?,
    @SerializedName("clouds")
    val cloudsResponse: CloudsResponse?,
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("coord")
    val coordResponse: CoordResponse?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val mainResponse: MainResponse?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("sys")
    val sysResponse: SysResponse?,
    @SerializedName("timezone")
    val timezone: Int?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weatherResponse: List<WeatherResponse>?,
    @SerializedName("wind")
    val wind: WindResponse?
) {

    fun mapToDomain() = CurrentDayModel(
        base = base,
        clouds = Clouds(cloudsResponse?.all),
        cod = cod,
        coord = Coord(coordResponse?.lat, coordResponse?.lon),
        dt = dt,
        id = id,
        main = Main(
            feelsLike = mainResponse?.feelsLike,
            grndLevel = mainResponse?.grndLevel,
            humidity = mainResponse?.humidity,
            pressure = mainResponse?.pressure,
            seaLevel = mainResponse?.seaLevel,
            temp = mainResponse?.temp,
            tempMax = mainResponse?.tempMax,
            tempMin = mainResponse?.tempMin
        ),
        name = name,
        sys = Sys(
            country = sysResponse?.country,
            id = sysResponse?.id,
            sunrise = sysResponse?.sunrise,
            sunset = sysResponse?.sunset,
            type = sysResponse?.type
        ),
        timezone = timezone,
        visibility = visibility,
        weather = weatherResponse?.map {
            Weather(
                description = it.description,
                icon = it.icon,
                id = it.id,
                main = it.main
            )
        },
        wind = Wind(deg = wind?.deg, gust = wind?.gust, speed = wind?.speed)
    )
}
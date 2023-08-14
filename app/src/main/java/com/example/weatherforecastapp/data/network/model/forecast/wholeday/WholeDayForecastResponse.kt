package com.example.weatherforecastapp.data.network.model.forecast.wholeday

import com.example.weatherforecastapp.domain.model.*
import com.example.weatherforecastapp.domain.model.wholeday.City
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import com.example.weatherforecastapp.domain.model.wholeday.Forecast
import com.example.weatherforecastapp.domain.model.wholeday.Rain
import com.google.gson.annotations.SerializedName

data class WholeDayForecastResponse(
    @SerializedName("city")
    val city: CityResponse?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val list: List<ForecastResponse>?,
    @SerializedName("message")
    val message: Int?
) {

    fun mapToDomain(): WholeDayModel {
        return WholeDayModel(
            city = City(
                coord = Coord(lat = city?.coord?.lat, lon = city?.coord?.lon),
                country = city?.country,
                id = city?.id,
                name = city?.name,
                population = city?.population,
                sunrise = city?.sunrise,
                sunset = city?.sunset,
                timezone = city?.timezone
            ), cnt = cnt, cod = cod, list = list?.map {
                Forecast(
                    clouds = Clouds(all = it.clouds?.all),
                    dt = it.dt,
                    dtTxt = it.dtTxt,
                    main = Main(
                        feelsLike = it.main?.feelsLike,
                        grndLevel = it.main?.grndLevel,
                        humidity = it.main?.humidity,
                        pressure = it.main?.pressure,
                        seaLevel = it.main?.seaLevel,
                        temp = it.main?.temp,
                        tempMax = it.main?.tempMax,
                        tempMin = it.main?.tempMin
                    ),
                    pop = it.pop,
                    rain = Rain(h = it.rain?.h),
                    sys = Sys(
                        country = it.sys?.country,
                        id = it.sys?.id,
                        sunrise = it.sys?.sunrise,
                        sunset = it.sys?.sunset,
                        type = it.sys?.type
                    ),
                    visibility = it.visibility,
                    weather = it.weather?.map { weather ->
                        Weather(
                            description = weather.description,
                            icon = weather.icon,
                            id = weather.id,
                            main = weather.main
                        )
                    } ?: emptyList(),
                    wind = Wind(deg = it.wind?.deg, gust = it.wind?.gust, speed = it.wind?.speed)
                )
            } ?: emptyList(), message = message

        )
    }
}
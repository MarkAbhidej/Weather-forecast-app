package com.example.weatherforecastapp.data.network.api

import com.example.weatherforecastapp.data.network.model.forecast.currentday.CurrentForecastResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.WholeDayForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiInterface {

    @GET("weather")
    suspend fun getForecastByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<CurrentForecastResponse>

    @GET("forecast")
    suspend fun getWholeDayForecastByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<WholeDayForecastResponse>

    @GET("weather")
    suspend fun getForecastByLocation(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<CurrentForecastResponse>
}
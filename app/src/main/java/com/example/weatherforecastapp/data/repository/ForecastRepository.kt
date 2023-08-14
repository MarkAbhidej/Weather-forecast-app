package com.example.weatherforecastapp.data.repository

import com.example.weatherforecastapp.BuildConfig
import com.example.weatherforecastapp.data.network.api.OpenWeatherApiInterface
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ForecastRepository {

    suspend fun getCurrentForecastFromCityName(city: String): Flow<CurrentDayModel>

    suspend fun getCurrentForecastFromLocation(lat: Double, lon: Double): Flow<CurrentDayModel>

    suspend fun getWholeDayForecastByLocation(lat: Double, lon: Double): Flow<WholeDayModel>
}

class ForecastRepositoryImpl(private val api: OpenWeatherApiInterface) : ForecastRepository {

    companion object {
        private const val WEATHER_FORECAST_UNIT = "metric"
        private const val MESSAGE_API_ERROR = "fail to load data"
    }

    override suspend fun getCurrentForecastFromLocation(
        lat: Double,
        lon: Double
    ): Flow<CurrentDayModel> {
        return flow {
            val response = api.getForecastByLocation(
                lat,
                lon,
                BuildConfig.API_KEY,
                WEATHER_FORECAST_UNIT
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(body.mapToDomain())
                } else {
                    error(MESSAGE_API_ERROR)
                }
            }
        }
    }

    override suspend fun getCurrentForecastFromCityName(city: String): Flow<CurrentDayModel> {
        return flow {
            val response =
                api.getForecastByLocation(city, BuildConfig.API_KEY, WEATHER_FORECAST_UNIT)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(body.mapToDomain())
                } else {
                    error(MESSAGE_API_ERROR)
                }
            }
        }
    }

    override suspend fun getWholeDayForecastByLocation(
        lat: Double,
        lon: Double
    ): Flow<WholeDayModel> {

        return flow {
            val response = api.getWholeDayForecastByLocation(
                lat,
                lon,
                BuildConfig.API_KEY,
                WEATHER_FORECAST_UNIT
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(body.mapToDomain())
                } else {
                    error(MESSAGE_API_ERROR)
                }
            }
        }
    }

}

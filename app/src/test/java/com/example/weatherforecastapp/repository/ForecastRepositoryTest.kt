package com.example.weatherforecastapp.repository

import com.example.weatherforecastapp.data.network.api.OpenWeatherApiInterface
import com.example.weatherforecastapp.data.network.model.forecast.*
import com.example.weatherforecastapp.data.network.model.forecast.currentday.CurrentForecastResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.CityResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.WholeDayForecastResponse
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.data.repository.ForecastRepositoryImpl
import com.example.weatherforecastapp.domain.model.*
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ForecastRepositoryTest {
    private val api: OpenWeatherApiInterface = mockk()
    private lateinit var repository: ForecastRepository

    @BeforeEach
    fun setup() {
        repository = ForecastRepositoryImpl(api)
    }

    @Test
    fun `test getCurrentForecastFromLocation success`() = runTest {
        // Given
        coEvery {
            api.getForecastByLocation(
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.success(mockResponseFormForecastByLocation())

        // When
        val result = repository.getCurrentForecastFromLocation(0.0, 0.0)

        // Then
        result.collect {
            assertEquals("", it.base)
        }
    }

    @Test
    fun `test getCurrentForecastFromLocation error`() = runTest {
        // Given
        coEvery {
            api.getForecastByLocation(
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error(400, "error".toResponseBody())

        // When
        val result = repository.getCurrentForecastFromLocation(0.0, 0.0)

        // Then
        result.catch { error ->
            assertEquals("fail to load data", error.message)
        }.collect()
    }

    @Test
    fun `test getCurrentForecastFromCityName success`() = runTest {
        // Given
        coEvery {
            api.getForecastByLocation(
                city = any(),
                apiKey = any(),
                units = any()
            )
        } returns Response.success(mockResponseFormForecastByCityName())

        // When
        val result = repository.getCurrentForecastFromCityName("Amsterdam")

        // Then
        result.collect {
            assertEquals("", it.base)
        }
    }

    @Test
    fun `test getCurrentForecastFromCityName error`() = runTest {
        // Given
        coEvery {
            api.getForecastByLocation(
                city = any(),
                apiKey = any(),
                units = any()
            )
        } returns Response.error(400, "error".toResponseBody())

        // When
        val result = repository.getCurrentForecastFromCityName("Amsterdam")

        // Then
        result.catch { error ->
            assertEquals("fail to load data", error.message)
        }.collect()
    }

    @Test
    fun `test getWholeDayForecastByLocation success`() = runTest {
        // Given
        coEvery {
            api.getWholeDayForecastByLocation(
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.success(mockResponseWholeDayForecast())

        // When
        val result = repository.getWholeDayForecastByLocation(0.0, 0.0)

        // Then
        result.collect {
            assertEquals("", it.city?.name)
        }
    }

    @Test
    fun `test getWholeDayForecastByLocation error`() = runTest {
        // Given
        coEvery {
            api.getWholeDayForecastByLocation(
                any(),
                any(),
                any(),
                any()
            )
        } returns Response.error(400, "error".toResponseBody())

        // When
        val result = repository.getWholeDayForecastByLocation(0.0, 0.0)

        // Then
        result.catch { error ->
            assertEquals("fail to load data", error.message)
        }.collect()
    }

    private fun mockResponseFormForecastByLocation(): CurrentForecastResponse {
        return CurrentForecastResponse(
            base = "",
            cloudsResponse = CloudsResponse(all = 1),
            cod = 1,
            coordResponse = CoordResponse(lat = 0.0, lon = 0.0),
            dt = 1,
            id = 1,
            mainResponse = MainResponse(
                feelsLike = 0.0,
                grndLevel = 1,
                humidity = 1,
                pressure = 1,
                seaLevel = 1,
                temp = 0.0,
                tempKf = 0.0,
                tempMax = 0.0,
                tempMin = 0.0
            ),
            name = "",
            sysResponse = SysResponse(
                country = "",
                id = 1,
                sunrise = 1,
                sunset = 1,
                type = 1,
                pod = ""
            ),
            timezone = 1,
            visibility = 1,
            weatherResponse = listOf(),
            wind = WindResponse(
                deg = 1,
                gust = 0.0,
                speed = 0.0
            )
        )
    }

    private fun mockResponseFormForecastByCityName(): CurrentForecastResponse {
        return CurrentForecastResponse(
            base = "",
            cloudsResponse = CloudsResponse(all = 1),
            cod = 1,
            coordResponse = CoordResponse(lat = 0.0, lon = 0.0),
            dt = 1,
            id = 1,
            mainResponse = MainResponse(
                feelsLike = 0.0,
                grndLevel = 1,
                humidity = 1,
                pressure = 1,
                seaLevel = 1,
                temp = 0.0,
                tempKf = 0.0,
                tempMax = 0.0,
                tempMin = 0.0
            ),
            name = "",
            sysResponse = SysResponse(
                country = "",
                id = 1,
                sunrise = 1,
                sunset = 1,
                type = 1,
                pod = ""
            ),
            timezone = 1,
            visibility = 1,
            weatherResponse = listOf(),
            wind = WindResponse(
                deg = 1,
                gust = 0.0,
                speed = 0.0
            )
        )
    }

    private fun mockResponseWholeDayForecast(): WholeDayForecastResponse {
        return WholeDayForecastResponse(
            city = CityResponse(
                coord = CoordResponse(
                    lat = 0.0,
                    lon = 0.0
                ),
                country = "",
                id = 1,
                name = "",
                population = 1,
                sunrise = 1,
                sunset = 1,
                timezone = 1
            ),
            cnt = 1,
            cod = "",
            list = listOf(),
            message = 1
        )
    }

    private fun mockToDayOpenWeather(): CurrentDayModel {
        return CurrentDayModel(
            base = "",
            clouds = Clouds(all = 1),
            cod = 1,
            coord = Coord(lat = 0.0, lon = 0.0),
            dt = 1,
            id = 1,
            main = Main(
                feelsLike = 0.0,
                grndLevel = 1,
                humidity = 1,
                pressure = 1,
                seaLevel = 1,
                temp = 0.0,
                tempMax = 0.0,
                tempMin = 0.0
            ),
            name = "",
            sys = Sys(
                country = "",
                id = 1,
                sunrise = 1,
                sunset = 1,
                type = 1
            ),
            timezone = 1,
            visibility = 1,
            weather = listOf<Weather>(),
            wind = Wind(
                deg = 1,
                gust = 0.0,
                speed = 0.0
            )
        )
    }
}
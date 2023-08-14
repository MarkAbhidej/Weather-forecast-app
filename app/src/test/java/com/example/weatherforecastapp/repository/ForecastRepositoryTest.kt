package com.example.weatherforecastapp.repository

import com.example.weatherforecastapp.data.network.api.OpenWeatherApiInterface
import com.example.weatherforecastapp.data.network.model.forecast.*
import com.example.weatherforecastapp.data.network.model.forecast.currentday.CurrentForecastResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.CityResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.ForecastResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.RainResponse
import com.example.weatherforecastapp.data.network.model.forecast.wholeday.WholeDayForecastResponse
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.data.repository.ForecastRepositoryImpl
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
            assertEquals(17.1, it.main?.temp)
            assertEquals(60, it.main?.humidity)
            assertEquals("Amsterdam", it.sys?.country)
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
        val result = repository.getCurrentForecastFromCityName("Bangkok")

        // Then
        result.collect {
            assertEquals(29.5, it.main?.temp)
            assertEquals(67, it.main?.humidity)
            assertEquals("Bangkok", it.sys?.country)
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
            assertEquals("Tokyo", it.city?.country)
            assertEquals(77, it.list?.first()?.main?.humidity)
            assertEquals(18.5, it.list?.first()?.main?.temp)
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
                humidity = 60,
                pressure = 1,
                seaLevel = 1,
                temp = 17.1,
                tempKf = 0.0,
                tempMax = 0.0,
                tempMin = 0.0
            ),
            name = "",
            sysResponse = SysResponse(
                country = "Amsterdam",
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
                humidity = 67,
                pressure = 1,
                seaLevel = 1,
                temp = 29.5,
                tempKf = 0.0,
                tempMax = 0.0,
                tempMin = 0.0
            ),
            name = "",
            sysResponse = SysResponse(
                country = "Bangkok",
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
                country = "Tokyo",
                id = 1,
                name = "",
                population = 1,
                sunrise = 1,
                sunset = 1,
                timezone = 1
            ),
            cnt = 1,
            cod = "",
            list = listOf(
                ForecastResponse(
                    clouds = CloudsResponse(
                        all = 1
                    ),
                    dt = 1,
                    dtTxt = "",
                    main = MainResponse(
                        feelsLike = 0.0,
                        grndLevel = 1,
                        humidity = 77,
                        pressure = 1,
                        seaLevel = 1,
                        temp = 18.5,
                        tempKf = 0.0,
                        tempMax = 0.0,
                        tempMin = 0.0
                    ),
                    pop = 0.0,
                    rain = RainResponse(
                        h = 0.0
                    ),
                    sys = SysResponse(
                        country = "",
                        id = 1,
                        sunrise = 1,
                        sunset = 1,
                        type = 1,
                        pod = ""
                    ),
                    visibility = 1,
                    weather = listOf(),
                    wind = WindResponse(
                        deg = 1,
                        gust = 0.0,
                        speed = 0.0
                    )
                )
            ),
            message = 1
        )
    }
}
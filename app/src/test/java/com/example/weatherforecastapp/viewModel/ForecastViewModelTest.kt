package com.example.weatherforecastapp.viewModel

import android.location.Address
import com.example.weatherforecastapp.domain.model.*
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.example.weatherforecastapp.domain.model.wholeday.City
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromCityNameUseCase
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromLocationUseCase
import com.example.weatherforecastapp.domain.usecase.GetWholeDayForecastByLocationUseCase
import com.example.weatherforecastapp.presentation.ForecastViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import io.mockk.*

@ExperimentalCoroutinesApi
class ForecastViewModelTest {
    private lateinit var forecastViewModel: ForecastViewModel
    private val getCurrentForecastFromCityNameUseCase: GetCurrentForecastFromCityNameUseCase =
        mockk()
    private val getCurrentForecastFromLocationUseCase: GetCurrentForecastFromLocationUseCase =
        mockk()
    private val getWholeDayForecastByLocationUseCase: GetWholeDayForecastByLocationUseCase = mockk()
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setup() {
        forecastViewModel = ForecastViewModel(
            getCurrentForecastFromCityNameUseCase,
            getCurrentForecastFromLocationUseCase,
            getWholeDayForecastByLocationUseCase
        )
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test getCurrentForecastFromLocation success`() = runTest {
        val mockAddress = mockk<Address>(relaxed = true)
        val mockCurrentDayModel = mockCurrentDayModel()
        coEvery {
            getCurrentForecastFromLocationUseCase.execute(any(), any())
        } returns flow {
            emit(mockCurrentDayModel)
        }

        forecastViewModel.getCurrentForecastFromLocation(mockAddress)
        coVerify { getCurrentForecastFromLocationUseCase.execute(any(), any()) }
    }

    @Test
    fun `test getCurrentForecastFormCityName success`() = runTest {
        val cityName = "Amsterdam"
        val mockCurrentDayModel = mockCurrentDayModel()
        coEvery {
            getCurrentForecastFromCityNameUseCase.execute(cityName)
        } returns flow {
            emit(mockCurrentDayModel)
        }
        forecastViewModel.getCurrentForecastFormCityName(cityName)
        coVerify { getCurrentForecastFromCityNameUseCase.execute(any()) }
    }

    @Test
    fun `test getWholeDayForecast success`() = runTest {
        val mockWholeDayModel = mockWholeDayModel()
        coEvery {
            getWholeDayForecastByLocationUseCase.execute(any(), any())
        } returns flow {
            emit(mockWholeDayModel)
        }
        forecastViewModel.getWholeDayForecast()
        coVerify { getWholeDayForecastByLocationUseCase.execute(any(), any()) }
    }

    private fun mockCurrentDayModel(): CurrentDayModel {
        return CurrentDayModel(
            base = "",
            clouds = Clouds(
                all = 1
            ),
            cod = 1,
            coord = Coord(
                lat = 0.0,
                lon = 0.0
            ),
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
            weather = listOf(),
            wind = Wind(
                deg = 1,
                gust = 0.0,
                speed = 0.0
            )
        )
    }

    private fun mockWholeDayModel(): WholeDayModel {
        return WholeDayModel(
            city = City(
                coord = Coord(
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
}
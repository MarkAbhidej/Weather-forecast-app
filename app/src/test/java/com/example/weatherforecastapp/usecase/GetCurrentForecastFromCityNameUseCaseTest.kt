package com.example.weatherforecastapp.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromCityNameUseCase
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromCityNameUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

@ExperimentalCoroutinesApi
class GetCurrentForecastFromCityNameUseCaseTest {
    private val repository: ForecastRepository = mockk()
    private lateinit var useCase: GetCurrentForecastFromCityNameUseCase

    @BeforeEach
    fun setup() {
        useCase = GetCurrentForecastFromCityNameUseCaseImpl(repository)
    }

    @Test
    fun `test GetCurrentForecastFromCityNameUseCase success`() = runTest {
        // Given
        val cityName = "Amsterdam"
        val expectedFlow: Flow<CurrentDayModel> = flowOf(mockk())
        coEvery {
            repository.getCurrentForecastFromCityName(cityName)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(cityName)

        // Then
        assert(resultFlow == expectedFlow)
    }

    @Test
    fun `test GetCurrentForecastFromCityNameUseCase error`() = runTest {
        // Given
        val cityName = "Amsterdam"
        val errorMessage = "Error fetching forecast"
        val expectedFlow: Flow<CurrentDayModel> = flow { throw Exception(errorMessage) }
        coEvery {
            repository.getCurrentForecastFromCityName(cityName)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(cityName)

        // Then
        assertThrows<Exception> {
            resultFlow.single()
        }
    }
}
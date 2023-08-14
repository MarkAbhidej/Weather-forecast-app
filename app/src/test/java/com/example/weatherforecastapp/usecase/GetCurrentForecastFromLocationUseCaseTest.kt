package com.example.weatherforecastapp.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromLocationUseCase
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromLocationUseCaseImpl
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
class GetCurrentForecastFromLocationUseCaseTest {
    private val repository: ForecastRepository = mockk()
    private lateinit var useCase: GetCurrentForecastFromLocationUseCase

    @BeforeEach
    fun setup() {
        useCase = GetCurrentForecastFromLocationUseCaseImpl(repository)
    }

    @Test
    fun `test GetCurrentForecastFromLocationUseCase success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val expectedFlow: Flow<CurrentDayModel> = flowOf(mockk())
        coEvery {
            repository.getCurrentForecastFromLocation(lat, lon)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(lat, lon)

        // Then
        assert(resultFlow == expectedFlow)
    }

    @Test
    fun `test GetCurrentForecastFromLocationUseCase error`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Error fetching forecast"
        val expectedFlow: Flow<CurrentDayModel> = flow { throw Exception(errorMessage) }
        coEvery {
            repository.getCurrentForecastFromLocation(lat, lon)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(lat, lon)

        // Then
        assertThrows<Exception> {
            resultFlow.single()
        }
    }
}
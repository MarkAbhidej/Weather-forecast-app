package com.example.weatherforecastapp.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import com.example.weatherforecastapp.domain.usecase.GetWholeDayForecastByLocationUseCase
import com.example.weatherforecastapp.domain.usecase.GetWholeDayForecastByLocationUseCaseImpl
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
class GetWholeDayForecastByLocationUseCaseTest {
    private val repository: ForecastRepository = mockk()
    private lateinit var useCase: GetWholeDayForecastByLocationUseCase

    @BeforeEach
    fun setup() {
        useCase = GetWholeDayForecastByLocationUseCaseImpl(repository)
    }

    @Test
    fun `test GetWholeDayForecastByLocationUseCase success`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val expectedFlow: Flow<WholeDayModel> = flowOf(mockk())
        coEvery {
            repository.getWholeDayForecastByLocation(lat, lon)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(lat, lon)

        // Then
        assert(resultFlow == expectedFlow)
    }

    @Test
    fun `test GetWholeDayForecastByLocationUseCase error`() = runTest {
        // Given
        val lat = 10.0
        val lon = 20.0
        val errorMessage = "Error fetching forecast"
        val expectedFlow: Flow<WholeDayModel> = flow { throw Exception(errorMessage) }
        coEvery {
            repository.getWholeDayForecastByLocation(lat, lon)
        } returns expectedFlow

        // When
        val resultFlow = useCase.execute(lat, lon)

        // Then
        assertThrows<Exception> {
            resultFlow.single()
        }
    }
}
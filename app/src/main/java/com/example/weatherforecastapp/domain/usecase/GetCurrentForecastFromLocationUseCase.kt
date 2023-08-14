package com.example.weatherforecastapp.domain.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import kotlinx.coroutines.flow.Flow

interface GetCurrentForecastFromLocationUseCase {
    suspend fun execute(lat: Double, lon: Double): Flow<CurrentDayModel>
}

class GetCurrentForecastFromLocationUseCaseImpl(private val repository: ForecastRepository): GetCurrentForecastFromLocationUseCase {
    override suspend fun execute(lat: Double, lon: Double): Flow<CurrentDayModel> {
        return repository.getCurrentForecastFromLocation(lat, lon)
    }

}
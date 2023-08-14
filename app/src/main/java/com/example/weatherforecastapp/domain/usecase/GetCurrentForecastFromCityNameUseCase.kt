package com.example.weatherforecastapp.domain.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import kotlinx.coroutines.flow.Flow

interface GetCurrentForecastFromCityNameUseCase {
    suspend fun execute(request: String): Flow<CurrentDayModel>
}

class GetCurrentForecastFromCityNameUseCaseImpl(private val repository: ForecastRepository) :
    GetCurrentForecastFromCityNameUseCase {
    override suspend fun execute(request: String): Flow<CurrentDayModel> {
        return repository.getCurrentForecastFromCityName(request)
    }

}
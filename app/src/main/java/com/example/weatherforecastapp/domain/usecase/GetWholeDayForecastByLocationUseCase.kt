package com.example.weatherforecastapp.domain.usecase

import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import kotlinx.coroutines.flow.Flow

interface GetWholeDayForecastByLocationUseCase {
    suspend fun execute(lat: Double, lon: Double): Flow<WholeDayModel>
}

class GetWholeDayForecastByLocationUseCaseImpl(private val repository: ForecastRepository): GetWholeDayForecastByLocationUseCase {
    override suspend fun execute(lat: Double, lon: Double): Flow<WholeDayModel> {
        return repository.getWholeDayForecastByLocation(lat, lon)
    }

}
package com.example.weatherforecastapp.presentation.di

import com.example.weatherforecastapp.presentation.ForecastViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val viewModelModule = module {
    viewModel {
        ForecastViewModel(
            getCurrentForecastFromCityNameUseCase = get(),
            getCurrentForecastFromLocationUseCase = get(),
            getWholeDayForecastByLocationUseCase = get()
        )
    }
}
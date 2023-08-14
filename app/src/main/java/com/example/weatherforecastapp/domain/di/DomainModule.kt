package com.example.weatherforecastapp.domain.di

import com.example.weatherforecastapp.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    factory<GetCurrentForecastFromCityNameUseCase> {
        GetCurrentForecastFromCityNameUseCaseImpl(get())
    }

    factory<GetCurrentForecastFromLocationUseCase> {
        GetCurrentForecastFromLocationUseCaseImpl(get())
    }

    factory<GetWholeDayForecastByLocationUseCase> {
        GetWholeDayForecastByLocationUseCaseImpl(get())
    }
}
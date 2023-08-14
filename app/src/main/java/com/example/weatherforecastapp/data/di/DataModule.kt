package com.example.weatherforecastapp.data.di

import com.example.weatherforecastapp.data.RetrofitClient
import com.example.weatherforecastapp.data.network.api.OpenWeatherApiInterface
import com.example.weatherforecastapp.data.repository.ForecastRepository
import com.example.weatherforecastapp.data.repository.ForecastRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {

    single {
        RetrofitClient.create()
    }

    factory {
        get<Retrofit>().create(OpenWeatherApiInterface::class.java)
    }

    factory<ForecastRepository> {
        ForecastRepositoryImpl(get())
    }
}
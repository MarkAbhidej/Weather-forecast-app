package com.example.weatherforecastapp

import android.app.Application
import com.example.weatherforecastapp.data.di.dataModule
import com.example.weatherforecastapp.domain.di.domainModule
import com.example.weatherforecastapp.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, domainModule, viewModelModule))
        }
    }
}
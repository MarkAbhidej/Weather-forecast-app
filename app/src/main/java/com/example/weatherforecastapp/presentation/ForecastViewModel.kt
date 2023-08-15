package com.example.weatherforecastapp.presentation

import android.location.Address
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.domain.model.currentday.CurrentDayModel
import com.example.weatherforecastapp.domain.model.wholeday.WholeDayModel
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromCityNameUseCase
import com.example.weatherforecastapp.domain.usecase.GetCurrentForecastFromLocationUseCase
import com.example.weatherforecastapp.domain.usecase.GetWholeDayForecastByLocationUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getCurrentForecastFromCityNameUseCase: GetCurrentForecastFromCityNameUseCase,
    private val getCurrentForecastFromLocationUseCase: GetCurrentForecastFromLocationUseCase,
    private val getWholeDayForecastByLocationUseCase: GetWholeDayForecastByLocationUseCase
) : ViewModel() {

    private var location: Address? = null
    private val onShowCitySearch = MutableLiveData<CurrentDayModel>()
    private val onShowCurrentForecast = MutableLiveData<CurrentDayModel>()
    private val onShowCurrentForecastError = MutableLiveData<Unit>()
    private val onShowKeywordSuggest = MutableLiveData<List<String>>()
    private val onShowWholeDayForecast = MutableLiveData<WholeDayModel>()
    private val onShowWholeDayForecastError = MutableLiveData<Unit>()

    fun onShowCitySearch() = onShowCitySearch
    fun onShowCurrentForecast() = onShowCurrentForecast
    fun onShowCurrentForecastError() = onShowCurrentForecastError
    fun onShowKeywordSuggest() = onShowKeywordSuggest
    fun onShowWholeDayForecast() = onShowWholeDayForecast
    fun onShowWholeDayForecastError() = onShowWholeDayForecastError

    fun getCurrentForecastFromLocation(address: Address? = location) {
        viewModelScope.launch {
            location = address
            address?.let { address ->
                getCurrentForecastFromLocationUseCase.execute(address.latitude, address.longitude)
                    .flowOn(Dispatchers.IO)
                    .catch {
                        onShowCurrentForecastError.value = Unit
                    }
                    .collect {
                        onShowCurrentForecast.value = it
                    }
            }
        }
    }

    fun getCurrentForecastFormCityName(search: String = "") {
        viewModelScope.launch {
            if (search.isNotBlank()) {
                getCurrentForecastFromCityNameUseCase.execute(search)
                    .flowOn(Dispatchers.IO)
                    .catch {
                        onShowCurrentForecastError.value = Unit
                    }
                    .collect {
                        onShowCitySearch.value = it
                    }
            }
        }
    }

    fun getWholeDayForecast() {
        viewModelScope.launch {
            val latitude = location?.latitude ?: 0.0
            val longitude = location?.longitude ?: 0.0
            getWholeDayForecastByLocationUseCase.execute(latitude, longitude)
                .flowOn(Dispatchers.IO)
                .catch {
                    onShowWholeDayForecastError.value = Unit
                }.collect {
                    onShowWholeDayForecast.value = it
                }
        }
    }

    fun setupCityList(json: String) {
        val sType = object : TypeToken<List<String>>() {}.type
        val gson = Gson().fromJson<List<String>>(json, sType)
        onShowKeywordSuggest.value = gson
    }
}
package com.example.weatherforecastapp.domain.model.wholeday

data class WholeDayModel(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<Forecast>?,
    val message: Int?
)
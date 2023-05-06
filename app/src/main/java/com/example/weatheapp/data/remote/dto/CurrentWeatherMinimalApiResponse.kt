package com.example.weatheapp.data.remote.dto

data class CurrentWeatherMinimalApiResponse(
    val main: MainWeatherMinimalData,
    val wind: WindMinimalData
)

data class MainWeatherMinimalData(
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int
)

data class WindMinimalData(
    val speed: Double
)
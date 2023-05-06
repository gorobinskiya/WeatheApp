package com.example.weatheapp.data.remote.dto

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val visibility: Int,
    val name: String,
    val cod: Int,
    val message: String? = null
)
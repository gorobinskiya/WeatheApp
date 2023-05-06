package com.example.weatheapp.data.remote.dto

data class CurrentWeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val visibility: Int,
    val name: String,
    val dt: Long
)
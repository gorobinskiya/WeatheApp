package com.example.weatheapp.data.remote.dto

data class Main(
    val temp: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val tempMin: Double,
    val tempMax: Double
)
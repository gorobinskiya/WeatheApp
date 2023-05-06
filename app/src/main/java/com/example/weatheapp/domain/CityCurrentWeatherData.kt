package com.example.weatheapp.domain

class CityCurrentWeatherData(
    name: String,
    lat: Double,
    lon: Double,
    val temp: Double,
    val pressure: Int,
    val tempMin: Double,
    val tempMax: Double,
    val windSpeed: Double,
) : CityData(name, lat, lon)
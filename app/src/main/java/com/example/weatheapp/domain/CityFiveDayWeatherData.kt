package com.example.weatheapp.domain

class CityFiveDayWeatherData(
    val date: String,
    val tempMin: Int,
    val tempMax: Int,
    name: String,
    lat: Double,
    lon: Double
) : CityData(name, lat, lon)
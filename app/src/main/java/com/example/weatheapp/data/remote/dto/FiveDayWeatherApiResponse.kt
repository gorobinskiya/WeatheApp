package com.example.weatheapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FiveDayWeatherApiResponse(
    val list: List<WeatherEntry>
)

data class WeatherEntry(
    @SerializedName("dt_txt") val dtTxt: String,
    val main: MainData
)

data class MainData(
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)
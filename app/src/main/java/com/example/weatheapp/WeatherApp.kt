package com.example.weatheapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {

    val openWeatherMapApiKey: String
        get() = getString(R.string.openweathermap_api_key)
}
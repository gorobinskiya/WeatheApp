package com.example.weatheapp.data

import android.icu.text.SimpleDateFormat
import com.example.weatheapp.domain.CityFiveDayWeatherData
import java.util.*
import kotlin.math.max
import kotlin.math.min

fun List<CityFiveDayWeatherData>.processWeatherData(): List<CityFiveDayWeatherData> {
    val dailyTemperatureData = mutableMapOf<String, Pair<Int, Int>>()

    for (weatherData in this) {
        val currentDate = weatherData.date.substring(0, 10)

        if (!dailyTemperatureData.containsKey(currentDate)) {
            dailyTemperatureData[currentDate] = Pair(weatherData.tempMin, weatherData.tempMax)
        } else {
            val currentMinMax = dailyTemperatureData[currentDate]!!
            val newMin = min(currentMinMax.first, weatherData.tempMin)
            val newMax = max(currentMinMax.second, weatherData.tempMax)
            dailyTemperatureData[currentDate] = Pair(newMin, newMax)
        }
    }

    val resultList = mutableListOf<CityFiveDayWeatherData>()

    for ((date, minMax) in dailyTemperatureData) {
        val dateWithDayOfWeek = getDayOfWeekFromDate(date)
        val cityWeatherData = CityFiveDayWeatherData(
            date = dateWithDayOfWeek,
            tempMin = minMax.first,
            tempMax = minMax.second,
            name = this[0].name,
            lat = this[0].lat,
            lon = this[0].lon
        )
        resultList.add(cityWeatherData)
    }

    return resultList
}

fun getDayOfWeekFromDate(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val parsedDate = inputFormat.parse(date) ?: return date
    return outputFormat.format(parsedDate)
}
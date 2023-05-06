package com.example.weatheapp.data

import android.util.Log
import com.example.weatheapp.domain.CityCurrentWeatherData
import com.example.weatheapp.domain.CityData
import com.example.weatheapp.domain.CityFiveDayWeatherData
import com.example.weatheapp.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.round

class WeatherDataManager @Inject constructor(private val cityRepository: CityRepository) {
    companion object {
        private const val SEARCH_CITY_RESULT_LIMIT = 1
    }

    suspend fun loadCitiesData(cities: Array<String>, apiKey: String): List<CityData> {
        val cityDataList = mutableListOf<CityData>()

        for (city in cities) {
            Log.d("WeatherDataManager", "Searching for city: $city, apiKey: $apiKey")
            val cityData = searchCity(city, SEARCH_CITY_RESULT_LIMIT, apiKey)
            if (cityData != null) {
                Log.d("WeatherDataManager", "Found city data: $cityData")
                val roundedLat = round(cityData.lat * 100) / 100
                val roundedLon = round(cityData.lon * 100) / 100
                val roundedCityData = CityData(cityData.name, roundedLat, roundedLon)
                cityDataList.add(roundedCityData)
            }
        }
        return cityDataList
    }

    private suspend fun searchCity(cityName: String, resultLimit: Int, apiKey: String): CityData? {
        return withContext(Dispatchers.IO) {
            Log.d("WeatherDataManager", "Searching for city: $cityName, resultLimit: $resultLimit, apiKey: $apiKey")
            val cityDataList = cityRepository.searchCity(cityName, resultLimit, apiKey)

            if (cityDataList != null && cityDataList.isNotEmpty()) {
                Log.d("WeatherDataManager", "Found city data list: $cityDataList")
                cityDataList[0]
            } else {
                Log.e("WeatherDataManager", "City not found or empty city data list: $cityName")
                null
            }
        }
    }

    suspend fun loadCityCurrentWeatherData(
        cityDataList: List<CityData>,
        apiKey: String
    ): List<CityCurrentWeatherData> {
        val cityCurrentWeatherDataList = mutableListOf<CityCurrentWeatherData>()

        for (cityData in cityDataList) {
            val currentWeatherApiResponse =
                cityRepository.getCityCurrentWeatherData(cityData.lat, cityData.lon, apiKey)
            if (currentWeatherApiResponse != null) {
                val cityCurrentWeatherData = CityCurrentWeatherData(
                    cityData.name,
                    cityData.lat,
                    cityData.lon,
                    currentWeatherApiResponse.main.temp,
                    currentWeatherApiResponse.main.pressure,
                    currentWeatherApiResponse.main.tempMin,
                    currentWeatherApiResponse.main.tempMax,
                    currentWeatherApiResponse.wind.speed
                )
                cityCurrentWeatherDataList.add(cityCurrentWeatherData)
            } else {
                Log.e(
                    "WeatherDataManager",
                    "Failed to load current weather data for city: ${cityData.name}"
                )
            }
        }
        return cityCurrentWeatherDataList
    }

    suspend fun loadCityFiveDayWeatherData(
        cityData: CityData,
        apiKey: String
    ): List<CityFiveDayWeatherData> {
        val weatherDataList = mutableListOf<CityFiveDayWeatherData>()

        val fiveDayWeatherApiResponse =
            cityRepository.getCityFiveDayWeatherData(cityData.lat, cityData.lon, apiKey)

        if (fiveDayWeatherApiResponse != null) {
            for (weatherEntry in fiveDayWeatherApiResponse.list) {
                val date = weatherEntry.dtTxt
                val tempMin = weatherEntry.main.tempMin.toInt()
                val tempMax = weatherEntry.main.tempMax.toInt()

                val cityFiveDayWeatherData = CityFiveDayWeatherData(
                    date,
                    tempMin,
                    tempMax,
                    cityData.name,
                    cityData.lat,
                    cityData.lon
                )
                weatherDataList.add(cityFiveDayWeatherData)
                Log.d(
                    "WeatherDataManager",
                    "City: ${cityData.name}, Date: $date, TempMin: $tempMin, TempMax: $tempMax"
                )
            }
        } else {
            Log.e(
                "WeatherDataManager",
                "Failed to load 5-day weather data for city: ${cityData.name}"
            )
        }

        return weatherDataList
    }
}
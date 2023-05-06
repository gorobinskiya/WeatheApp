package com.example.weatheapp.repository

import android.util.Log
import com.example.weatheapp.data.remote.APIWeatherInterface
import com.example.weatheapp.data.remote.dto.CurrentWeatherMinimalApiResponse
import com.example.weatheapp.data.remote.dto.FiveDayWeatherApiResponse
import com.example.weatheapp.domain.CityData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(private val api: APIWeatherInterface) {

    suspend fun searchCity(cityName: String, resultLimit: Int, apiKey: String): List<CityData>? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CityRepository", "Searching for city: $cityName, resultLimit: $resultLimit, apiKey: $apiKey")
                val response = api.searchCity(cityName, resultLimit, apiKey).execute()
                if (response.isSuccessful) {
                    Log.d("CityRepository", "Received city data: ${response.body()}")
                    response.body()
                } else {
                    Log.e("CityRepository", "Unsuccessful response searching city: $cityName, response code: ${response.code()}, message: ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("CityRepository", "Exception while searching city: $cityName, message: ${e.message}", e)
                null
            }
        }
    }

    suspend fun getCityCurrentWeatherData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): CurrentWeatherMinimalApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CityRepository", "Getting current weather data for lat: $lat, lon: $lon, apiKey: $apiKey")
                val response = api.getCurrentWeather(lat, lon, apiKey)
                Log.d("CityRepository", "Received current weather data: $response")
                response
            } catch (e: Exception) {
                Log.e("CityRepository", "Failed to get current weather data: ${e.message}", e)
                null
            }
        }
    }

    suspend fun getCityFiveDayWeatherData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): FiveDayWeatherApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("CityRepository", "Getting 5-day weather data for lat: $lat, lon: $lon, apiKey: $apiKey")
                val response = api.getCityFiveDayWeatherData(lat, lon, apiKey)
                Log.d("CityRepository", "Received 5-day weather data: $response")
                response
            } catch (e: Exception) {
                Log.e("CityRepository", "Failed to get 5-day weather data: ${e.message}", e)
                null
            }
        }
    }
}
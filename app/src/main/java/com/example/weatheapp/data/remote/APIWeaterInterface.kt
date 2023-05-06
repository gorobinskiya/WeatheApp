package com.example.weatheapp.data.remote

import com.example.weatheapp.data.remote.dto.CurrentWeatherMinimalApiResponse
import com.example.weatheapp.data.remote.dto.FiveDayWeatherApiResponse
import com.example.weatheapp.domain.CityData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIWeatherInterface {
    @GET("geo/1.0/direct")
    fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Call<List<CityData>>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherMinimalApiResponse

    @GET("data/2.5/forecast")
    suspend fun getCityFiveDayWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): FiveDayWeatherApiResponse
}

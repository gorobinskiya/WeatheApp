package com.example.weatheapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiveDayWeather(fiveDayWeather: FiveDayWeatherEntity)

    @Query("SELECT * FROM current_weather WHERE name = :cityName")
    suspend fun getCurrentWeather(cityName: String): CurrentWeatherEntity?

    @Query("SELECT * FROM current_weather")
    suspend fun getAllCurrentWeather(): List<CurrentWeatherEntity>

    @Query("SELECT * FROM five_day_weather WHERE cityName = :cityName")
    suspend fun getFiveDayWeather(cityName: String): List<FiveDayWeatherEntity>
}
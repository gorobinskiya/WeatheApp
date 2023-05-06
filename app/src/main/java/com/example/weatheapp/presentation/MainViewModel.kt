package com.example.weatheapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.data.WeatherDataManager
import com.example.weatheapp.data.local.*
import com.example.weatheapp.data.processWeatherData
import com.example.weatheapp.domain.CityCurrentWeatherData
import com.example.weatheapp.domain.CityData
import com.example.weatheapp.domain.CityFiveDayWeatherData
import com.example.weatheapp.utils.isConnectedToInternet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val weatherDataManager: WeatherDataManager,
    private val apiKey: String,
    private val cities: Array<String>,
    private val weatherDao: WeatherDao
) : AndroidViewModel(application) {

    private val _cityDataList = MutableLiveData<List<CityData>>()
    val cityDataList: LiveData<List<CityData>> = _cityDataList

    private val _cityCurrentWeatherDataList = MutableLiveData<List<CityCurrentWeatherData>>()
    val cityCurrentWeatherDataList: LiveData<List<CityCurrentWeatherData>> = _cityCurrentWeatherDataList

    private val _cityFiveDayWeatherDataMap = MutableLiveData<Map<String, List<CityFiveDayWeatherData>>>()
    val cityFiveDayWeatherDataMap: LiveData<Map<String, List<CityFiveDayWeatherData>>> = _cityFiveDayWeatherDataMap

    private val _selectedCity = MutableLiveData<CityCurrentWeatherData>()
    val selectedCity: LiveData<CityCurrentWeatherData> = _selectedCity

    init {
        loadData()
    }

    private fun loadData() {
        Log.d("ViewModelMainActivity", "loadData() called")
        viewModelScope.launch {
            val cityDataList = loadCitiesData()

            if (isConnectedToInternet(getApplication())) {
                loadCityCurrentWeatherData(cityDataList)
                loadCityFiveDayWeatherData(cityDataList)
            } else {
                loadCurrentWeatherDataFromRoom()
                loadFiveDayWeatherDataFromRoom()
            }
        }
    }

    private suspend fun loadCitiesData(): List<CityData> {
        return try {
            val cityDataList = withContext(Dispatchers.IO) {
                weatherDataManager.loadCitiesData(cities, apiKey)
            }
            _cityDataList.value = cityDataList
            Log.d("ViewModelMainActivity", "Loaded cities data: $cityDataList")
            cityDataList

        } catch (e: Exception) {
            Log.e("MainActivityViewModel", "Error loading cities data: ${e.message}", e)
            emptyList()
        }
    }

    private suspend fun loadCityCurrentWeatherData(cityDataList: List<CityData>) {
        try {
            val cityCurrentWeatherDataList = withContext(Dispatchers.IO) {
                weatherDataManager.loadCityCurrentWeatherData(cityDataList, apiKey)
            }
            _cityCurrentWeatherDataList.value = cityCurrentWeatherDataList
            saveCurrentWeatherDataToRoom(cityCurrentWeatherDataList)

        } catch (e: Exception) {
            Log.e("MainActivityViewModel", "Error loading current weather data: ${e.message}", e)
        }
    }

    private fun loadCityFiveDayWeatherData(cityDataList: List<CityData>) {
        viewModelScope.launch {
            try {
                val cityFiveDayWeatherDataMap = mutableMapOf<String, List<CityFiveDayWeatherData>>()
                for (cityData in cityDataList) {
                    val cityFiveDayWeatherDataList =
                        weatherDataManager.loadCityFiveDayWeatherData(cityData, apiKey)
                    val processedCityWeatherDataList =
                        cityFiveDayWeatherDataList.processWeatherData()
                    cityFiveDayWeatherDataMap[cityData.name] = processedCityWeatherDataList
                }
                _cityFiveDayWeatherDataMap.value = cityFiveDayWeatherDataMap
                saveFiveDayWeatherDataToRoom(cityFiveDayWeatherDataMap)

            } catch (e: Exception) {
                Log.e(
                    "MainActivityViewModel", "Error loading five-day weather data: ${e.message}", e
                )
            }
        }
    }

    private suspend fun saveCurrentWeatherDataToRoom(cityCurrentWeatherDataList: List<CityCurrentWeatherData>) {
        withContext(Dispatchers.IO) {
            cityCurrentWeatherDataList.forEach { cityCurrentWeatherData ->
                val entity = cityCurrentWeatherData.toCurrentWeatherEntity()
                weatherDao.insertCurrentWeather(entity)
                Log.d(
                    "ViewModelMainActivity",
                    "Saved current weather data for city: ${cityCurrentWeatherData.name}"
                )
            }
        }
    }

    private suspend fun saveFiveDayWeatherDataToRoom(cityFiveDayWeatherDataMap: Map<String, List<CityFiveDayWeatherData>>) {
        withContext(Dispatchers.IO) {
            cityFiveDayWeatherDataMap.forEach { (cityName, cityWeatherDataList) ->
                cityWeatherDataList.forEach { cityFiveDayWeatherData ->
                    val entity = cityFiveDayWeatherData.toFiveDayWeatherEntity()
                    weatherDao.insertFiveDayWeather(entity)
                    Log.d(
                        "ViewModelMainActivity",
                        "Saved five-day weather data for city: $cityName, date: ${cityFiveDayWeatherData.date}"
                    )
                }
            }
        }
    }

    private suspend fun loadCurrentWeatherDataFromRoom() {
        try {
            withContext(Dispatchers.IO) {
                val currentWeatherEntityList = weatherDao.getAllCurrentWeather()
                val cityCurrentWeatherDataList =
                    currentWeatherEntityList.map { it.toCityCurrentWeatherData() }

                withContext(Dispatchers.Main) {
                    _cityCurrentWeatherDataList.value = cityCurrentWeatherDataList
                }
            }
        } catch (e: Exception) {
            Log.e(
                "ViewModelMainActivity",
                "Error loading current weather data from Room: ${e.message}",
                e
            )
        }
    }

    private suspend fun loadFiveDayWeatherDataFromRoom() {
        try {
            withContext(Dispatchers.IO) {
                val cityNames = cities.toList()
                val cityFiveDayWeatherDataMap = mutableMapOf<String, List<CityFiveDayWeatherData>>()
                for (cityName in cityNames) {
                    val fiveDayWeatherEntityList = weatherDao.getFiveDayWeather(cityName)
                    val cityFiveDayWeatherDataList =
                        fiveDayWeatherEntityList.map { it.toCityFiveDayWeatherData() }
                    cityFiveDayWeatherDataMap[cityName] = cityFiveDayWeatherDataList
                }
                withContext(Dispatchers.Main) {
                    _cityFiveDayWeatherDataMap.value = cityFiveDayWeatherDataMap
                }
            }
        } catch (e: Exception) {
            Log.e(
                "ViewModelMainActivity",
                "Error loading five-day weather data from Room: ${e.message}",
                e
            )
        }
    }

    fun setSelectedCity(cityName: String) {
        Log.d("ViewModelMainActivity", "Setting selected city: $cityName")
        _cityCurrentWeatherDataList.value?.let { cityDailyWeatherDataList ->
            val city = cityDailyWeatherDataList.find { it.name == cityName }
            if (city != null) {
                _selectedCity.value = city!!
            } else {
                Log.e("ViewModelMainActivity", "City not found: $cityName")
            }
        } ?: run {
            Log.e("ViewModelMainActivity", "CityDailyWeatherDataList is null")
        }
    }
}
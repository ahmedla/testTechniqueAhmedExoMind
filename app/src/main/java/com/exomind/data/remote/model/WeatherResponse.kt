package com.exomind.data.remote.model

data class WeatherResponse(
    val name: String,
    val weather: List<Weather>?,
    val main: MainWeatherInfo?
)

data class Weather(
    val description: String,
    val icon: String
)

data class MainWeatherInfo(
    val temp: Double
)
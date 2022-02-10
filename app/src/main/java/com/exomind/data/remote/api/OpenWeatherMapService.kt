package com.exomind.data.remote.api

import com.exomind.BuildConfig
import com.exomind.data.remote.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    /**
     * Returns current weather data for one location (city name)
     * @param (required) {Constants.QUERY_PARAM_CITY} => "q"
     */
    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query(Constants.QUERY_PARAM_CITY) cityName: String,
        @Query(Constants.QUERY_PARAM_API_KEY) apiKey: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>
}
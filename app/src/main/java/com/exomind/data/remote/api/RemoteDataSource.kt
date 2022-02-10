package com.exomind.data.remote.api

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val dataService: OpenWeatherMapService
) : BaseDataSource() {

    suspend fun getWeatherByCityName(name: String) =
        getResult { dataService.getWeatherByCityName(cityName = name) }
}
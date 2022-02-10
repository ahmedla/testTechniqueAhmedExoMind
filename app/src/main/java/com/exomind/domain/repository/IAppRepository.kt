package com.exomind.domain.repository

import com.exomind.data.remote.model.WeatherResponse
import com.exomind.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    suspend fun getWeatherByCityName(name: String): Flow<Resource<WeatherResponse>>
}
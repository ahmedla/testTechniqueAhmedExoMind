package com.exomind.domain.usecase.base

import com.exomind.data.utils.Resource
import com.exomind.domain.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

abstract class GetWeatherByCityNameBaseUseCase {
    abstract suspend operator fun invoke(params: String): Flow<Resource<WeatherEntity>>
}
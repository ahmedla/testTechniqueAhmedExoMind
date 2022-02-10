package com.exomind.domain.usecase

import com.exomind.data.utils.Resource
import com.exomind.domain.model.WeatherEntity
import com.exomind.domain.repository.IAppRepository
import com.exomind.domain.usecase.base.GetWeatherByCityNameBaseUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeatherByCityNameUseCase @Inject constructor(
    private val repository: IAppRepository
) : GetWeatherByCityNameBaseUseCase() {

    /**
     * This use case is used to map WeatherResponse to WeatherEntity
     */
    @FlowPreview
    override suspend fun invoke(params: String): Flow<Resource<WeatherEntity>> {
        return repository
            .getWeatherByCityName(params)
            .flatMapMerge { res ->
                flow {
                    when (res.status) {
                        Resource.Status.LOADING -> emit(
                            Resource.loading(null)
                        )
                        Resource.Status.ERROR -> emit(
                            Resource.error(res.message ?: "", null)
                        )
                        Resource.Status.SUCCESS -> emit(
                            Resource.success(
                                WeatherEntity(
                                    res.data?.name,
                                    ICON_BASE_URL + res.data?.weather?.first()?.icon + ICON_EXT,
                                    res.data?.main?.temp
                                )
                            )
                        )
                    }
                }
            }
    }

    companion object {
        private const val ICON_BASE_URL = "https://openweathermap.org/img/w/"
        private const val ICON_EXT = ".png"
    }
}
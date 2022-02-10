package com.exomind.data.repository

import com.exomind.data.remote.api.RemoteDataSource
import com.exomind.data.utils.Resource
import com.exomind.domain.repository.IAppRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IAppRepository {

    override suspend fun getWeatherByCityName(name: String) = flow {
        emit(Resource.loading())
        try {
            emit(remoteDataSource.getWeatherByCityName(name))
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "Error Occurred!"
                )
            )
        }
    }
}
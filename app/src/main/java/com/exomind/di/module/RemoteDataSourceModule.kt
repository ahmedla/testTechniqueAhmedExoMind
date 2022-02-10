package com.exomind.di.module

import com.exomind.data.remote.api.OpenWeatherMapService
import com.exomind.data.remote.api.RemoteDataSource
import com.exomind.data.repository.AppRepository
import com.exomind.domain.repository.IAppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(dataService: OpenWeatherMapService) =
        RemoteDataSource(dataService)

    @Provides
    @Singleton
    fun provideAppRepository(
        remoteDataSource: RemoteDataSource
    ): IAppRepository {
        return AppRepository(remoteDataSource)
    }
}
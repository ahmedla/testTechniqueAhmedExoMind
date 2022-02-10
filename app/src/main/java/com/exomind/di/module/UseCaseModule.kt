package com.exomind.di.module

import com.exomind.data.repository.AppRepository
import com.exomind.domain.usecase.GetWeatherByCityNameUseCase
import com.exomind.domain.usecase.base.GetWeatherByCityNameBaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetWeatherByCityNameUseCase(
        appRepository: AppRepository
    ): GetWeatherByCityNameBaseUseCase {
        return GetWeatherByCityNameUseCase(appRepository)
    }
}
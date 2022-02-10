package com.exomind.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exomind.base.BaseViewModel
import com.exomind.data.utils.Resource
import com.exomind.domain.model.WeatherEntity
import com.exomind.domain.usecase.base.GetWeatherByCityNameBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetWeatherByCityNameBaseUseCase
) : BaseViewModel() {

    /**
     * ProgressState : Holding Resource of progress value
     * TODO: Move these to strings.xml
     */
    private val cities = listOf(
        "Rennes",
        "Paris",
        "Nantes",
        "Bordeaux",
        "Lyon"
    )

    fun numberOfCities() = cities.size

    private val waitingMessages = listOf(
        "Nous téléchargeons les données…",
        "C’est presque fini…",
        "Plus que quelques secondes avant d’avoir le résultat…"
    )

    /**
     * WeatherState : Holding Resource of WeatherEntity
     */
    private val _weatherState = MutableStateFlow(
        Resource.loading(WeatherEntity(DEFAULT_STRING_VALUE))
    )
    val weatherState: StateFlow<Resource<WeatherEntity>> = _weatherState

    /**
     * ProgressState : Holding progress value
     */
    private var progressValue = PROGRESS_INITIAL_VALUE
    private val _progressState = MutableLiveData(progressValue)
    val progressState: LiveData<Int> = _progressState

    /**
     * WaitingMessageState : Holding waiting message that changes every 6sec
     */
    private val _waitingMessageState = MutableLiveData(waitingMessages.first())
    val waitingMessageState: LiveData<String> = _waitingMessageState

    fun incrementProgress() {
        progressValue += PROGRESS_INCREMENT
        _progressState.value = progressValue
    }

    fun resetProgress() {
        progressValue = PROGRESS_INITIAL_VALUE
        _progressState.value = progressValue
    }

    /**
     * New waiting message Job
     * TODO: Move code into UseCase
     */
    fun sendNewWaitingMessage() {
        launch {
            while (progressValue < PROGRESS_COMPLETE_VALUE) {
                waitingMessages.map {
                    _waitingMessageState.postValue(it)
                    delay(DELAY_MESSAGE_TIME)
                }
            }
        }
    }

    /**
     * Fetching Weather Job
     * TODO: Move code into UseCase
     */
    var fetchWeatherJob: Job? = null

    private fun stopFetchWeatherJob() {
        fetchWeatherJob?.cancel()
        fetchWeatherJob = null
    }

    private fun fetchCityWeather(name: String) {
        launch {
            useCase
                .invoke(name)
                .collect { weatherData ->
                    _weatherState.value = weatherData
                }
        }
    }

    fun startFetchingWeather() {
        stopFetchWeatherJob()

        fetchWeatherJob = launch {
            while (progressValue < PROGRESS_COMPLETE_VALUE) {
                cities.map {
                    fetchCityWeather(it) // the function that should be ran every second
                    delay(DELAY_FETCHING_TIME)
                }
            }
        }
    }

    /**
     * Companion object
     */
    companion object {
        private const val PROGRESS_INCREMENT = 15
        private const val PROGRESS_INITIAL_VALUE = 0
        private const val PROGRESS_COMPLETE_VALUE = 100
        private const val DELAY_FETCHING_TIME = 10000L
        private const val DELAY_MESSAGE_TIME = 6000L
        private const val DEFAULT_STRING_VALUE = ""
    }
}
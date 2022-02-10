package com.exomind.domain.model

data class WeatherEntity(
    val name: String?,
    val icon: String? = null,
    val temperature: Double? = null
)
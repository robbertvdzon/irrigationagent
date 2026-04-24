package com.vdzon.irrigation.weatherforecast

import java.time.LocalDate

data class WeatherForecast(
    val date: LocalDate,
    val rainExpectedMm: Double
)

interface WeatherForecastPort {
    suspend fun getForecast(date: LocalDate): WeatherForecast
    suspend fun getAllForecasts(): List<WeatherForecast>
}

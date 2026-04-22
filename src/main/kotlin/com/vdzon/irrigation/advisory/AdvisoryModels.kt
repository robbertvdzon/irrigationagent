package com.vdzon.irrigation.advisory

import java.time.LocalDate

data class WeatherForecast(
    val forecastDate: LocalDate,
    val rainExpectedMm: Double
)

data class RainHistory(
    val date: LocalDate,
    val rainMm: Double
)

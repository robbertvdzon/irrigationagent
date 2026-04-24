package com.vdzon.irrigation.weatherforecast

import java.time.LocalDate

/**
 * Representation of the weather forecast for a specific day.
 */
data class WeatherForecast(
    /** The date of the weather forecast. */
    val date: LocalDate,
    /** The expected amount of precipitation in mm. */
    val rainExpectedMm: Double,
    /** The expected maximum temperature in degrees Celsius. */
    val maxTempCelsius: Double
)

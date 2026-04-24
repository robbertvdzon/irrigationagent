package com.vdzon.irrigation.weatherforecast

import java.time.LocalDate

/**
 * The public gateway for the weather forecast module.
 * Used by the advisory module to make decisions based on the weather forecast.
 */
interface WeatherForecastPort {
    /**
     * Fetches the forecast for a specific date.
     * If necessary, the data is refreshed from the external weather service.
     */
    suspend fun getForecast(date: LocalDate): WeatherForecast

    /**
     * Provides an overview of all saved forecasts.
     */
    suspend fun getAllForecasts(): List<WeatherForecast>
}

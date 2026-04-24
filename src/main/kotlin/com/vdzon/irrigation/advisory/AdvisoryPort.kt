package com.vdzon.irrigation.advisory

import com.vdzon.irrigation.weatherforecast.WeatherForecast
import com.vdzon.irrigation.rainhistory.RainHistory
import java.time.LocalDate

/**
 * The public interface for the advisory module.
 * This module is responsible for calculating irrigation advice based on weather forecast and rain history.
 */
interface AdvisoryPort {
    /**
     * Calculates the advice for the specified date and publishes an IrrigationProposed event.
     * Fetches data from WeatherForecastPort and RainHistoryPort for this purpose.
     */
    suspend fun calculateAndProposeAdvice(date: LocalDate)
}

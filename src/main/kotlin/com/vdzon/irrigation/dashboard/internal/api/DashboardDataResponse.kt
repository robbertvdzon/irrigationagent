package com.vdzon.irrigation.dashboard.internal.api

import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import java.time.LocalDate
import java.time.LocalDateTime

data class DashboardDataResponse(
    val forecasts: List<WeatherForecast>,
    val history: List<RainHistory>,
    val advices: List<IrrigationAdvice>,
    val advice: IrrigationAdvice?,
    val events: List<IrrigationEvent>
)

data class WeatherForecast(
    val date: LocalDate,
    val rainExpectedMm: Double,
    val maxTempCelsius: Double
)

data class RainHistory(
    val date: LocalDate,
    val rainMm: Double
)

data class IrrigationAdvice(
    val date: LocalDate,
    val durationMinutes: Int,
    val status: String
)

data class IrrigationEvent(
    val eventDate: LocalDateTime,
    val durationMinutes: Int,
    val status: String
)

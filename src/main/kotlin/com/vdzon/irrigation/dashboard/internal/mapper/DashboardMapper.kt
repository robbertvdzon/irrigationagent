package com.vdzon.irrigation.dashboard.internal.mapper

import com.vdzon.irrigation.dashboard.internal.api.*
import com.vdzon.irrigation.dashboard.internal.model.DashboardData

fun DashboardData.toResponse(): DashboardDataResponse {
    return DashboardDataResponse(
        forecasts = forecasts.map { it.toResponse() },
        history = history.map { it.toResponse() },
        advices = advices.map { it.toResponse() },
        advice = advice?.toResponse(),
        events = events.map { it.toResponse() }
    )
}

private fun com.vdzon.irrigation.weatherforecast.WeatherForecast.toResponse(): WeatherForecast {
    return WeatherForecast(
        date = date,
        rainExpectedMm = rainExpectedMm,
        maxTempCelsius = maxTempCelsius
    )
}

private fun com.vdzon.irrigation.rainhistory.RainHistory.toResponse(): RainHistory {
    return RainHistory(
        date = date,
        rainMm = rainMm
    )
}

private fun com.vdzon.irrigation.advisory.IrrigationAdvice.toResponse(): IrrigationAdvice {
    return IrrigationAdvice(
        date = date,
        durationMinutes = durationMinutes,
        status = status
    )
}

private fun com.vdzon.irrigation.irrigation.IrrigationEvent.toResponse(): IrrigationEvent {
    return IrrigationEvent(
        eventDate = eventDate,
        durationMinutes = durationMinutes,
        status = status
    )
}

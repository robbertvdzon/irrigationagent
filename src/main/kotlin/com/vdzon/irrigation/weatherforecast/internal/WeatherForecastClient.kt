package com.vdzon.irrigation.weatherforecast.internal

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

data class ForecastResponse(
    val date: String,
    val rainMm: Double?,// TODO: why nullable?
    val maxTemp: Double?// TODO:  why nullable?
)

@HttpExchange("/forecast")
interface WeatherForecastClient {
    @GetExchange
    suspend fun getDailyForecast(): ForecastResponse
}

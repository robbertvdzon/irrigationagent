package com.vdzon.irrigation.weatherforecast.internal

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

data class ForecastResponse(
    val date: String,
    val rainMm: Double,
    val maxTemp: Double
)

@HttpExchange("/forecast")
interface WeatherForecastClient {
    @GetExchange
    @Retry(name = "weatherForecast")
    @CircuitBreaker(name = "weatherForecast")
    @RateLimiter(name = "weatherForecast")
    @TimeLimiter(name = "weatherForecast")
    @Bulkhead(name = "weatherForecast")
    suspend fun getDailyForecast(): ForecastResponse
}

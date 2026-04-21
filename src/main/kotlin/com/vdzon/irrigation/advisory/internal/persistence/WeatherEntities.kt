package com.vdzon.irrigation.advisory.internal.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("weather_forecasts")
data class WeatherForecastEntity(
    @Id
    var id: Int? = null,
    val forecastDate: LocalDate,
    val rainExpectedMm: Double,
    val fetchedAt: LocalDateTime = LocalDateTime.now()
)

@Table("rain_history")
data class RainHistoryEntity(
    @Id
    var id: Int? = null,
    val date: LocalDate,
    val rainMm: Double,
    val fetchedAt: LocalDateTime = LocalDateTime.now()
)

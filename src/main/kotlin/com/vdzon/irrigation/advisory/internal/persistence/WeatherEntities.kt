package com.vdzon.irrigation.advisory.internal.persistence

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "weather_forecasts")
class WeatherForecastEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    val forecastDate: LocalDate,
    val rainExpectedMm: Double,
    val fetchedAt: LocalDateTime = LocalDateTime.now()
)

@Entity
@Table(name = "rain_history")
class RainHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    val date: LocalDate,
    val rainMm: Double,
    val fetchedAt: LocalDateTime = LocalDateTime.now()
)

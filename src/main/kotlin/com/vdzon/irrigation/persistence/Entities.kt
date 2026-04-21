package com.vdzon.irrigation.persistence

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

@Entity
@Table(name = "irrigation_advices")
class IrrigationAdviceEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val date: LocalDate,
    var durationMinutes: Int, // Advies in minuten
    var status: String // PENDING, EXECUTED, MANUALLY_ADJUSTED
)

@Entity
@Table(name = "irrigation_events")
class IrrigationEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    val eventDate: LocalDateTime,
    val durationMinutes: Int,
    val status: String
)

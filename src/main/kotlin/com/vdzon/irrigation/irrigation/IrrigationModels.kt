package com.vdzon.irrigation.irrigation

import java.time.LocalDate
import java.time.LocalDateTime

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

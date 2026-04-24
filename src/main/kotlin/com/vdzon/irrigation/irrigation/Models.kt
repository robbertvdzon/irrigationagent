package com.vdzon.irrigation.irrigation

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Representation of an irrigation advice.
 */
data class IrrigationAdvice(
    /** The date for which the advice applies. */
    val date: LocalDate,
    /** The advised duration in minutes. */
    val durationMinutes: Int,
    /** The current status of the advice (e.g., PENDING, EXECUTED). */
    val status: String
)

/**
 * Representation of an executed irrigation activity.
 */
data class IrrigationEvent(
    /** The timestamp when the activity took place. */
    val eventDate: LocalDateTime,
    /** The actual duration in minutes. */
    val durationMinutes: Int,
    /** The status of the execution (e.g., COMPLETED). */
    val status: String
)

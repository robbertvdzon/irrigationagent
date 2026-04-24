package com.vdzon.irrigation.advisory

import java.time.LocalDate

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

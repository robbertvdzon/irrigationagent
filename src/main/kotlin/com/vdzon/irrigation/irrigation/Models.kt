package com.vdzon.irrigation.irrigation

import java.time.LocalDateTime

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

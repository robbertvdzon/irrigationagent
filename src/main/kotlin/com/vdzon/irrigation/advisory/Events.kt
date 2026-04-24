package com.vdzon.irrigation.advisory

import java.time.LocalDate

/**
 * Event that is published when a new irrigation advice has been calculated.
 * The 'notification' module listens to this event.
 */
data class IrrigationProposed(
    /** The date for which the advice applies. */
    val date: LocalDate,
    /** The proposed duration of the irrigation in minutes. */
    val durationMinutes: Int
)

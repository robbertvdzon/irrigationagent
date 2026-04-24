package com.vdzon.irrigation.rainhistory

import java.time.LocalDate

/**
 * Representation of the measured rainfall on a specific day.
 */
data class RainHistory(
    /** The date of the measurement. */
    val date: LocalDate,
    /** The amount of precipitation in mm. */
    val rainMm: Double
)

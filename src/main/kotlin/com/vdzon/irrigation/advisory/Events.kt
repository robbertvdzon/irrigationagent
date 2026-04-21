package com.vdzon.irrigation.advisory

import java.time.LocalDate

data class IrrigationProposed(
    val date: LocalDate,
    val durationMinutes: Int
)

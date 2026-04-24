package com.vdzon.irrigation.advisory.internal.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("irrigation_advices")
data class IrrigationAdviceEntity(
    @Id
    var id: Int? = null,
    val date: LocalDate,
    var durationMinutes: Int,
    var status: String // PENDING, EXECUTED, MANUALLY_ADJUSTED
)

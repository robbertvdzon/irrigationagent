package com.vdzon.irrigation.irrigation.internal.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("irrigation_advices")
data class IrrigationAdviceEntity(
    @Id
    var id: Int? = null,
    val date: LocalDate,
    var durationMinutes: Int,
    var status: String // PENDING, EXECUTED, MANUALLY_ADJUSTED
)

@Table("irrigation_events")
data class IrrigationEventEntity(
    @Id
    var id: Int? = null,
    val eventDate: LocalDateTime,
    val durationMinutes: Int,
    val status: String
)

package com.vdzon.irrigation.irrigation.internal.persistence

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "irrigation_advices")
class IrrigationAdviceEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    val date: LocalDate,
    var durationMinutes: Int,
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

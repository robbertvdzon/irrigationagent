package com.vdzon.irrigation.irrigation.internal.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface IrrigationAdviceRepository : JpaRepository<IrrigationAdviceEntity, Int> {
    fun findByDate(date: LocalDate): IrrigationAdviceEntity?
    fun findTop7ByOrderByDateDesc(): List<IrrigationAdviceEntity>
}

@Repository
interface IrrigationEventRepository : JpaRepository<IrrigationEventEntity, Int> {
    fun findTop7ByOrderByEventDateDesc(): List<IrrigationEventEntity>
}

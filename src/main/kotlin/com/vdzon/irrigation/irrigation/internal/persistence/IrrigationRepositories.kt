package com.vdzon.irrigation.irrigation.internal.persistence

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Repository
interface IrrigationAdviceRepository : CoroutineCrudRepository<IrrigationAdviceEntity, Int> {
    suspend fun findByDate(date: LocalDate): IrrigationAdviceEntity?
    fun findAllByOrderByDateDesc(): Flow<IrrigationAdviceEntity>
}

@Repository
interface IrrigationEventRepository : CoroutineCrudRepository<IrrigationEventEntity, Int> {
    fun findAllByOrderByEventDateDesc(): Flow<IrrigationEventEntity>
}

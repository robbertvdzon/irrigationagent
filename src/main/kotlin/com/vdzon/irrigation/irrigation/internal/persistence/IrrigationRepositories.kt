package com.vdzon.irrigation.irrigation.internal.persistence

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import kotlinx.coroutines.flow.Flow

@Repository
interface IrrigationEventRepository : CoroutineCrudRepository<IrrigationEventEntity, Int> {
    fun findAllByOrderByEventDateDesc(): Flow<IrrigationEventEntity>
}

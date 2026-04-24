package com.vdzon.irrigation.rainhistory.internal.persistence

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RainHistoryRepository : CoroutineCrudRepository<RainHistoryEntity, Int> {
    suspend fun findByDate(date: LocalDate): RainHistoryEntity?
    fun findAllByOrderByDateDesc(): Flow<RainHistoryEntity>
}

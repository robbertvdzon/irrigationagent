package com.vdzon.irrigation.advisory.internal.persistence

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Repository
interface WeatherForecastRepository : CoroutineCrudRepository<WeatherForecastEntity, Int> {
    suspend fun findByForecastDate(date: LocalDate): WeatherForecastEntity?
    fun findAllByOrderByForecastDateDesc(): Flow<WeatherForecastEntity>
}

@Repository
interface RainHistoryRepository : CoroutineCrudRepository<RainHistoryEntity, Int> {
    suspend fun findByDate(date: LocalDate): RainHistoryEntity?
    fun findAllByOrderByDateDesc(): Flow<RainHistoryEntity>
}

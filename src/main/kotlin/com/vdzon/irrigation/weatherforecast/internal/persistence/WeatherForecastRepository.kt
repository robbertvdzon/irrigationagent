package com.vdzon.irrigation.weatherforecast.internal.persistence

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface WeatherForecastRepository : CoroutineCrudRepository<WeatherForecastEntity, Int> {
    suspend fun findByForecastDate(date: LocalDate): WeatherForecastEntity?
    fun findAllByOrderByForecastDateDesc(): Flow<WeatherForecastEntity>
}

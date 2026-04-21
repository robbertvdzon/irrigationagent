package com.vdzon.irrigation.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface WeatherForecastRepository : JpaRepository<WeatherForecastEntity, Int> {
    fun findByForecastDate(date: LocalDate): WeatherForecastEntity?
    fun findTop7ByOrderByForecastDateDesc(): List<WeatherForecastEntity>
}

@Repository
interface RainHistoryRepository : JpaRepository<RainHistoryEntity, Int> {
    fun findByDate(date: LocalDate): RainHistoryEntity?
    fun findTop7ByOrderByDateDesc(): List<RainHistoryEntity>
}

@Repository
interface IrrigationAdviceRepository : JpaRepository<IrrigationAdviceEntity, Int> {
    fun findByDate(date: LocalDate): IrrigationAdviceEntity?
    fun findTop7ByOrderByDateDesc(): List<IrrigationAdviceEntity>
}

@Repository
interface IrrigationEventRepository : JpaRepository<IrrigationEventEntity, Int> {
    fun findTop7ByOrderByEventDateDesc(): List<IrrigationEventEntity>
}

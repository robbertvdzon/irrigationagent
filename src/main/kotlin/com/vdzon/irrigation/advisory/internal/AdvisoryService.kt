package com.vdzon.irrigation.advisory.internal

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.weatherforecast.WeatherForecastPort
import com.vdzon.irrigation.rainhistory.RainHistoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

@Service
class AdvisoryService(
    private val weatherForecastPort: WeatherForecastPort,
    private val rainHistoryPort: RainHistoryPort,
    private val irrigationPort: IrrigationPort,
    private val irrigationAdviceRepository: IrrigationAdviceRepository,
    private val eventPublisher: ApplicationEventPublisher
) : AdvisoryPort {
    private val logger = LoggerFactory.getLogger(AdvisoryService::class.java)

    @Transactional
    override suspend fun calculateAndProposeAdvice(date: LocalDate) {
        logger.info("Calculating advice for $date")
        
        // 1. Fetch data
        val forecast = weatherForecastPort.getForecast(date)
        val history = rainHistoryPort.updateRainHistory(7)
        val irrigationEvents = irrigationPort.getEvents()

        // 2. Logic
        val totalRainLastWeek = history.sumOf { it.rainMm }
        val expectedRainToday = forecast.rainExpectedMm
        val maxTempToday = forecast.maxTempCelsius
        
        val yesterday = date.minusDays(1)
        val irrigatedYesterday = irrigationEvents.any { it.eventDate.toLocalDate() == yesterday && it.status == "COMPLETED" }

        var durationMinutes = 0
        if (expectedRainToday < 2.0 && totalRainLastWeek < 15.0) {
            if (maxTempToday > 25.0) {
                durationMinutes = 30
            } else {
                if (!irrigatedYesterday) {
                    durationMinutes = 15
                } else {
                    durationMinutes = 0
                }
            }
        }

        logger.info("Advice for $date: $durationMinutes minutes (Temp: $maxTempToday, Rain: $expectedRainToday, RainWeek: $totalRainLastWeek, IrrigatedYesterday: $irrigatedYesterday)")
        
        // Save advice
        saveAdvice(date, durationMinutes, "PENDING")

        // Publish event
        eventPublisher.publishEvent(IrrigationProposed(date, durationMinutes))
    }

    @Transactional
    override suspend fun saveAdvice(date: LocalDate, minutes: Int, status: String) {
        val advice = irrigationAdviceRepository.findByDate(date) ?: IrrigationAdviceEntity(date = date, durationMinutes = minutes, status = status)
        val updatedAdvice = advice.copy(durationMinutes = minutes, status = status)
        irrigationAdviceRepository.save(updatedAdvice)
        logger.info("Advice for $date saved with status $status")
    }

    override suspend fun getAdvices(): List<IrrigationAdvice> =
        irrigationAdviceRepository.findAllByOrderByDateDesc()
            .toList()
            .map { IrrigationAdvice(it.date, it.durationMinutes, it.status) }

    override suspend fun getTodayAdvice(): IrrigationAdvice? =
        irrigationAdviceRepository.findByDate(LocalDate.now())
            ?.let { IrrigationAdvice(it.date, it.durationMinutes, it.status) }
}

package com.vdzon.irrigation.advisory.internal.services

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.irrigation.IrrigationPort
import com.vdzon.irrigation.rainhistory.RainHistoryPort
import com.vdzon.irrigation.weatherforecast.WeatherForecastPort
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class AdvisoryService(
    private val weatherForecastPort: WeatherForecastPort,
    private val rainHistoryPort: RainHistoryPort,
    private val irrigationPort: IrrigationPort,
    private val irrigationAdviceRepository: IrrigationAdviceRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val advisoryCalculationService: AdvisoryCalculationService
) : AdvisoryPort {
    private val logger = LoggerFactory.getLogger(AdvisoryService::class.java)

    @Transactional
    override suspend fun calculateAndProposeAdvice(date: LocalDate) {
        logger.info("Calculating advice for $date")

        // Fetch data
        val forecast = weatherForecastPort.getForecast(date)
        val history = rainHistoryPort.updateRainHistory(7)
        val irrigationEvents = irrigationPort.getEvents()

        // create advice
        val irrigationProposed = advisoryCalculationService.calculateAndProposeAdvice(history, forecast, date, irrigationEvents)

        // Save advice
        saveAdvice(date, irrigationProposed.durationMinutes, "PENDING")

        // Publish event
        eventPublisher.publishEvent(irrigationProposed)
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
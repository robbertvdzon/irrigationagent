package com.vdzon.irrigation.advisory.internal

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.advisory.RainHistory
import com.vdzon.irrigation.advisory.WeatherForecast
import com.vdzon.irrigation.advisory.internal.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

@Service
class AdvisoryService(
    private val weatherClient: WeatherClient,
    private val weatherForecastRepository: WeatherForecastRepository,
    private val rainHistoryRepository: RainHistoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) : AdvisoryPort {
    private val logger = LoggerFactory.getLogger(AdvisoryService::class.java)

    @Transactional
    override suspend fun calculateAndProposeAdvice(date: LocalDate) {
        logger.info("Calculating advice for $date")
        
        // 1. Fetch data
        val forecast = weatherClient.getDailyForecast()
        val history = weatherClient.getRainHistory(7)

        // 2. Save fetched data
        weatherForecastRepository.save(
            WeatherForecastEntity(
                forecastDate = LocalDate.parse(forecast.date),
                rainExpectedMm = forecast.rainMm
            )
        )
        
        history.forEach {
            rainHistoryRepository.save(
                RainHistoryEntity(
                    date = LocalDate.parse(it.date),
                    rainMm = it.rainMm
                )
            )
        }

        // 3. Logic
        val totalRainLastWeek = history.sumOf { it.rainMm }
        val expectedRainToday = forecast.rainMm
        
        var durationMinutes = 0
        if (expectedRainToday < 2.0 && totalRainLastWeek < 15.0) {
            durationMinutes = 30
        }

        logger.info("Advice for $date: $durationMinutes minutes")
        
        // Publish event
        eventPublisher.publishEvent(IrrigationProposed(date, durationMinutes))
    }

    override suspend fun getForecasts(): List<WeatherForecast> =
        weatherForecastRepository.findAllByOrderByForecastDateDesc()
            .toList()
            .map { WeatherForecast(it.forecastDate, it.rainExpectedMm) }

    override suspend fun getRainHistory(): List<RainHistory> =
        rainHistoryRepository.findAllByOrderByDateDesc()
            .toList()
            .map { RainHistory(it.date, it.rainMm) }
}

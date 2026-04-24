package com.vdzon.irrigation.advisory.internal

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import com.vdzon.irrigation.weatherforecast.WeatherForecastPort
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.rainhistory.RainHistoryPort
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class AdvisoryService(
    private val weatherForecastPort: WeatherForecastPort,
    private val rainHistoryPort: RainHistoryPort,
    private val eventPublisher: ApplicationEventPublisher
) : AdvisoryPort {
    private val logger = LoggerFactory.getLogger(AdvisoryService::class.java)

    @Transactional
    override suspend fun calculateAndProposeAdvice(date: LocalDate) {
        logger.info("Calculating advice for $date")
        
        // 1. Fetch & Save data via ports
        val forecast = weatherForecastPort.getForecast(date)
        val history = rainHistoryPort.updateRainHistory(7)

        // 2. Logic
        val totalRainLastWeek = history.sumOf { it.rainMm }
        val expectedRainToday = forecast.rainExpectedMm
        
        var durationMinutes = 0
        if (expectedRainToday < 2.0 && totalRainLastWeek < 15.0) {
            durationMinutes = 30
        }

        logger.info("Advice for $date: $durationMinutes minutes")
        
        // Publish event
        eventPublisher.publishEvent(IrrigationProposed(date, durationMinutes))
    }

    override suspend fun getForecasts(): List<WeatherForecast> =
        weatherForecastPort.getAllForecasts()

    override suspend fun getRainHistory(): List<RainHistory> =
        rainHistoryPort.getRainHistory()
}

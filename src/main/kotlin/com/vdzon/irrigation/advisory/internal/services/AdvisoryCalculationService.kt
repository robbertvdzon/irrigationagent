package com.vdzon.irrigation.advisory.internal.services

import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AdvisoryCalculationService {
    private val logger = LoggerFactory.getLogger(AdvisoryService::class.java)


    fun calculateAndProposeAdvice(
        history: List<RainHistory>,
        forecast: WeatherForecast,
        date: LocalDate,
        irrigationEvents: List<IrrigationEvent>
    ): IrrigationProposed {
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
        logger.info("Advice for $date: ${durationMinutes} minutes (Temp: $maxTempToday, Rain: $expectedRainToday, RainWeek: $totalRainLastWeek, IrrigatedYesterday: $irrigatedYesterday)")

        val irrigationProposed = IrrigationProposed(date, durationMinutes)
        return irrigationProposed
    }

}
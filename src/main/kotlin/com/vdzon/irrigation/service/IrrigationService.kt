package com.vdzon.irrigation.service

import com.vdzon.irrigation.controller.IrrigationController
import com.vdzon.irrigation.persistence.*
import com.vdzon.irrigation.weather.WeatherClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class IrrigationService(
    private val weatherClient: WeatherClient,
    private val irrigationController: IrrigationController,
    private val weatherForecastRepository: WeatherForecastRepository,
    private val rainHistoryRepository: RainHistoryRepository,
    private val irrigationAdviceRepository: IrrigationAdviceRepository,
    private val irrigationEventRepository: IrrigationEventRepository
) {
    private val logger = LoggerFactory.getLogger(IrrigationService::class.java)

    @Transactional
    fun calculateAndSaveAdvice(date: LocalDate) {
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

        val advice = irrigationAdviceRepository.findByDate(date) ?: IrrigationAdviceEntity(date = date, durationMinutes = durationMinutes, status = "PENDING")
        advice.durationMinutes = durationMinutes
        advice.status = "PENDING"
        irrigationAdviceRepository.save(advice)
        
        logger.info("Advice for $date: $durationMinutes minutes (status: ${advice.status})")
    }

    @Transactional
    fun updateAdvice(date: LocalDate, minutes: Int) {
        val advice = irrigationAdviceRepository.findByDate(date) ?: IrrigationAdviceEntity(date = date, durationMinutes = minutes, status = "MANUALLY_ADJUSTED")
        advice.durationMinutes = minutes
        advice.status = "MANUALLY_ADJUSTED"
        irrigationAdviceRepository.save(advice)
        logger.info("Advice for $date manually updated to $minutes minutes")
    }

    @Transactional
    fun executeAdvice(date: LocalDate) {
        val advice = irrigationAdviceRepository.findByDate(date)
        if (advice == null || advice.durationMinutes <= 0) {
            logger.info("No irrigation needed or advice found for $date")
            return
        }

        if (advice.status == "EXECUTED") {
            logger.info("Advice for $date already executed")
            return
        }

        logger.info("Executing irrigation advice for $date: ${advice.durationMinutes} minutes")
        irrigationController.startIrrigation(advice.durationMinutes)
        
        advice.status = "EXECUTED"
        irrigationAdviceRepository.save(advice)

        irrigationEventRepository.save(
            IrrigationEventEntity(
                eventDate = LocalDateTime.now(),
                durationMinutes = advice.durationMinutes,
                status = "COMPLETED"
            )
        )
    }

    fun getForecasts() = weatherForecastRepository.findTop7ByOrderByForecastDateDesc()
    fun getRainHistory() = rainHistoryRepository.findTop7ByOrderByDateDesc()
    fun getAdvices() = irrigationAdviceRepository.findTop7ByOrderByDateDesc()
    fun getTodayAdvice() = irrigationAdviceRepository.findByDate(LocalDate.now())
    fun getEvents() = irrigationEventRepository.findTop7ByOrderByEventDateDesc()
}

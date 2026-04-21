package com.vdzon.irrigation.dashboard

import com.vdzon.irrigation.advisory.AdvisoryService
import com.vdzon.irrigation.irrigation.IrrigationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class IrrigationAgent(
    private val advisoryService: AdvisoryService,
    private val irrigationService: IrrigationService
) {
    private val logger = LoggerFactory.getLogger(IrrigationAgent::class.java)

    @Scheduled(cron = "0 0 6 * * *") // Elke dag om 6:00
    fun generateDailyAdvice() {
        logger.info("Generating daily advice (scheduled 06:00)")
        advisoryService.calculateAndProposeAdvice(LocalDate.now())
    }

    @Scheduled(cron = "0 30 7 * * *") // Elke dag om 7:30
    fun executeDailyAdvice() {
        logger.info("Executing daily advice (scheduled 07:30)")
        irrigationService.executeAdvice(LocalDate.now())
    }
}

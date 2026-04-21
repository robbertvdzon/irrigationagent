package com.vdzon.irrigation.dashboard.internal

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@Component
class IrrigationAgent(
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort
) {
    private val logger = LoggerFactory.getLogger(IrrigationAgent::class.java)

    @Scheduled(cron = "0 0 6 * * *") // Every day at 6:00
    fun generateDailyAdvice() = runBlocking {
        logger.info("Generating daily advice (scheduled 06:00)")
        advisoryPort.calculateAndProposeAdvice(LocalDate.now())
    }

    @Scheduled(cron = "0 30 7 * * *") // Every day at 7:30
    fun executeDailyAdvice() = runBlocking {
        logger.info("Executing daily advice (scheduled 07:30)")
        irrigationPort.executeAdvice(LocalDate.now())
    }
}

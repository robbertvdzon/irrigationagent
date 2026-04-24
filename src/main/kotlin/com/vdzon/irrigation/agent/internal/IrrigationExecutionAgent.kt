package com.vdzon.irrigation.agent.internal

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class IrrigationExecutionAgent(
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort
){
    private val logger = LoggerFactory.getLogger(IrrigationExecutionAgent::class.java)

    @Scheduled(cron = "0 0 6 * * *") // Every day at 6:00
    fun generateDailyAdvice() = runBlocking {
        logger.info("Generating daily advice (scheduled 06:00)")
        advisoryPort.calculateAndProposeAdvice(LocalDate.now())
    }

    @Scheduled(cron = "0 30 7 * * *") // Every day at 7:30
    fun executeDailyAdvice() = runBlocking {
        logger.info("Executing daily advice (scheduled 07:30)")
        val todayAdvice = advisoryPort.getTodayAdvice()
        if (todayAdvice != null && todayAdvice.durationMinutes > 0) {
            irrigationPort.startIrrigation(todayAdvice.durationMinutes)
            advisoryPort.saveAdvice(todayAdvice.date, todayAdvice.durationMinutes, "EXECUTED")
        }
    }
}

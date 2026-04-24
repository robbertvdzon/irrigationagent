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

    @Scheduled(cron = "\${irrigation.schedule.advice}") // Configured in application.yml
    fun generateDailyAdvice() = runBlocking {
        logger.info("Generating daily advice")
        advisoryPort.calculateAndProposeAdvice(LocalDate.now())
    }

    @Scheduled(cron = "\${irrigation.schedule.execution}") // Configured in application.yml
    fun executeDailyAdvice() = runBlocking {
        logger.info("Executing daily advice")
        val todayAdvice = advisoryPort.getTodayAdvice()
        if (todayAdvice != null && todayAdvice.durationMinutes > 0) {
            irrigationPort.startIrrigation(todayAdvice.durationMinutes)
            advisoryPort.saveAdvice(todayAdvice.date, todayAdvice.durationMinutes, "EXECUTED")
        }
    }
}

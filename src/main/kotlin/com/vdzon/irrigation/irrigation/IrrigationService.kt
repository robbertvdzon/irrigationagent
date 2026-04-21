package com.vdzon.irrigation.irrigation

import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.irrigation.internal.HardwareController
import com.vdzon.irrigation.irrigation.internal.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class IrrigationService(
    private val hardwareController: HardwareController,
    private val irrigationAdviceRepository: IrrigationAdviceRepository,
    private val irrigationEventRepository: IrrigationEventRepository
) {
    private val logger = LoggerFactory.getLogger(IrrigationService::class.java)

    @EventListener
    @Transactional
    fun onIrrigationProposed(event: IrrigationProposed) {
        logger.info("Received irrigation proposal for ${event.date}: ${event.durationMinutes} minutes")
        saveAdvice(event.date, event.durationMinutes, "PENDING")
    }

    @Transactional
    fun saveAdvice(date: LocalDate, minutes: Int, status: String) {
        val advice = irrigationAdviceRepository.findByDate(date) ?: IrrigationAdviceEntity(date = date, durationMinutes = minutes, status = status)
        advice.durationMinutes = minutes
        advice.status = status
        irrigationAdviceRepository.save(advice)
        logger.info("Advice for $date saved with status $status")
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
        hardwareController.startIrrigation(advice.durationMinutes)
        
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

    fun getAdvices() = irrigationAdviceRepository.findTop7ByOrderByDateDesc()
    fun getTodayAdvice() = irrigationAdviceRepository.findByDate(LocalDate.now())
    fun getEvents() = irrigationEventRepository.findTop7ByOrderByEventDateDesc()
}

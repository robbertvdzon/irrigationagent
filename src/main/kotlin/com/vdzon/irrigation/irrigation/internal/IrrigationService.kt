package com.vdzon.irrigation.irrigation.internal

import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.irrigation.IrrigationPort
import com.vdzon.irrigation.irrigation.internal.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class IrrigationService(
    private val hardwareIrrigationAdapter: HardwareIrrigationAdapter,
    private val irrigationAdviceRepository: IrrigationAdviceRepository,
    private val irrigationEventRepository: IrrigationEventRepository
) : IrrigationPort {
    private val logger = LoggerFactory.getLogger(IrrigationService::class.java)

    @EventListener
    @Transactional
    override suspend fun onIrrigationProposed(event: IrrigationProposed) {
        logger.info("Received irrigation proposal for ${event.date}: ${event.durationMinutes} minutes")
        saveAdvice(event.date, event.durationMinutes, "PENDING")
    }

    @Transactional
    override suspend fun saveAdvice(date: LocalDate, minutes: Int, status: String) {
        val advice = irrigationAdviceRepository.findByDate(date) ?: IrrigationAdviceEntity(date = date, durationMinutes = minutes, status = status)
        val updatedAdvice = advice.copy(durationMinutes = minutes, status = status)
        irrigationAdviceRepository.save(updatedAdvice)
        logger.info("Advice for $date saved with status $status")
    }

    @Transactional
    override suspend fun executeAdvice(date: LocalDate) {
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
        hardwareIrrigationAdapter.startIrrigation(advice.durationMinutes)
        
        val updatedAdvice = advice.copy(status = "EXECUTED")
        irrigationAdviceRepository.save(updatedAdvice)

        irrigationEventRepository.save(
            IrrigationEventEntity(
                eventDate = LocalDateTime.now(),
                durationMinutes = advice.durationMinutes,
                status = "COMPLETED"
            )
        )
    }

    override suspend fun getAdvices() = irrigationAdviceRepository.findAllByOrderByDateDesc().toList()
    override suspend fun getTodayAdvice() = irrigationAdviceRepository.findByDate(LocalDate.now())
    override suspend fun getEvents() = irrigationEventRepository.findAllByOrderByEventDateDesc().toList()
}

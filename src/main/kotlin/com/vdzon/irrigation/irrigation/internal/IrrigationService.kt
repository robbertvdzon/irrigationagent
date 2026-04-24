package com.vdzon.irrigation.irrigation.internal

import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.irrigation.IrrigationPort
import com.vdzon.irrigation.irrigation.internal.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime

@Service
class IrrigationService(
    private val hardwareIrrigationAdapter: HardwareIrrigationAdapter,
    private val irrigationEventRepository: IrrigationEventRepository
) : IrrigationPort {
    private val logger = LoggerFactory.getLogger(IrrigationService::class.java)

    @Transactional
    override suspend fun startIrrigation(minutes: Int) {
        if (minutes <= 0) {
            logger.info("No irrigation needed ($minutes minutes)")
            return
        }

        logger.info("Executing irrigation: $minutes minutes")
        hardwareIrrigationAdapter.startIrrigation(minutes)
        
        irrigationEventRepository.save(
            IrrigationEventEntity(
                eventDate = LocalDateTime.now(),
                durationMinutes = minutes,
                status = "COMPLETED"
            )
        )
    }

    override suspend fun getEvents(): List<IrrigationEvent> =
        irrigationEventRepository.findAllByOrderByEventDateDesc()
            .toList()
            .map { IrrigationEvent(it.eventDate, it.durationMinutes, it.status) }
}

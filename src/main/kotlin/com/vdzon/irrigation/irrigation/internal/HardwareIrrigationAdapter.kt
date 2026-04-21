package com.vdzon.irrigation.irrigation.internal

import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

@Component
class HardwareIrrigationAdapter {
    private val logger = LoggerFactory.getLogger(HardwareIrrigationAdapter::class.java)

    fun startIrrigation(durationMinutes: Int) {
        logger.info("Starting hardware irrigation for $durationMinutes minutes")
    }
}

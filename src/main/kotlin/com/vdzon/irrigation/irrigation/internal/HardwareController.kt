package com.vdzon.irrigation.irrigation.internal

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class HardwareController {
    private val logger = LoggerFactory.getLogger(HardwareController::class.java)

    fun startIrrigation(durationMinutes: Int) {
        logger.info("Starting hardware irrigation for $durationMinutes minutes")
    }
}

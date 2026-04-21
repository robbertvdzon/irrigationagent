package com.vdzon.irrigation.controller

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class IrrigationController {
    private val logger = LoggerFactory.getLogger(IrrigationController::class.java)

    fun startIrrigation(durationMinutes: Int) {
        logger.info("Starting irrigation for $durationMinutes minutes")
        // Hier zou normaal een call naar een fysiek apparaat of een andere service gaan
    }
}

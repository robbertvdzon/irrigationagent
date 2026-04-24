package com.vdzon.irrigation.irrigation

import java.time.LocalDate

/**
 * The public interface for the irrigation module.
 * Responsible for executing irrigation activity.
 */
interface IrrigationPort {
    /**
     * Executes the advice for the specified date via the hardware adapter.
     * Implementation should fetch the actual advice duration from the advisory module or be passed directly.
     * In this pure implementation, it might just take the minutes to irrigate.
     */
    suspend fun startIrrigation(minutes: Int)

    /**
     * Provides an overview of all executed irrigation events.
     */
    suspend fun getEvents(): List<IrrigationEvent>
}

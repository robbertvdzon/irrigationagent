package com.vdzon.irrigation.irrigation

import java.time.LocalDate

/**
 * The public interface for the irrigation module.
 * Responsible for saving and executing irrigation advice.
 */
interface IrrigationPort {
    /**
     * Saves a new advice or updates an existing advice.
     */
    suspend fun saveAdvice(date: LocalDate, minutes: Int, status: String)

    /**
     * Executes the advice for the specified date via the hardware adapter.
     */
    suspend fun executeAdvice(date: LocalDate)

    /**
     * Provides a list of all saved advice.
     */
    suspend fun getAdvices(): List<IrrigationAdvice>

    /**
     * Provides the advice for today, if present.
     */
    suspend fun getTodayAdvice(): IrrigationAdvice?

    /**
     * Provides an overview of all executed irrigation events.
     */
    suspend fun getEvents(): List<IrrigationEvent>
}

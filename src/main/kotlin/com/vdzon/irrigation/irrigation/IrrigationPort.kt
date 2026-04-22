package com.vdzon.irrigation.irrigation

import com.vdzon.irrigation.advisory.IrrigationProposed
import java.time.LocalDate

interface IrrigationPort {
    suspend fun onIrrigationProposed(event: IrrigationProposed)
    suspend fun saveAdvice(date: LocalDate, minutes: Int, status: String)
    suspend fun executeAdvice(date: LocalDate)
    suspend fun getAdvices(): List<IrrigationAdvice>
    suspend fun getTodayAdvice(): IrrigationAdvice?
    suspend fun getEvents(): List<IrrigationEvent>
}

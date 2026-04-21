package com.vdzon.irrigation.irrigation

import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity
import java.time.LocalDate

interface IrrigationPort {
    suspend fun onIrrigationProposed(event: IrrigationProposed)
    suspend fun saveAdvice(date: LocalDate, minutes: Int, status: String)
    suspend fun executeAdvice(date: LocalDate)
    suspend fun getAdvices(): List<IrrigationAdviceEntity>
    suspend fun getTodayAdvice(): IrrigationAdviceEntity?
    suspend fun getEvents(): List<IrrigationEventEntity>
}

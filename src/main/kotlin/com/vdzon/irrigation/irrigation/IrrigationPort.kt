package com.vdzon.irrigation.irrigation

import com.vdzon.irrigation.advisory.IrrigationProposed
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity
import java.time.LocalDate

interface IrrigationPort {
    fun onIrrigationProposed(event: IrrigationProposed)
    fun saveAdvice(date: LocalDate, minutes: Int, status: String)
    fun executeAdvice(date: LocalDate)
    fun getAdvices(): List<IrrigationAdviceEntity>
    fun getTodayAdvice(): IrrigationAdviceEntity?
    fun getEvents(): List<IrrigationEventEntity>
}

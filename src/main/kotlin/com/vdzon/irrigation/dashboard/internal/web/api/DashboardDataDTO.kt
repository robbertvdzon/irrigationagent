package com.vdzon.irrigation.dashboard.internal.web.api

import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity

data class DashboardDataDTO(
    val forecasts: List<WeatherForecastEntity>,
    val history: List<RainHistoryEntity>,
    val advices: List<IrrigationAdviceEntity>,
    val advice: IrrigationAdviceEntity?,
    val events: List<IrrigationEventEntity>
)

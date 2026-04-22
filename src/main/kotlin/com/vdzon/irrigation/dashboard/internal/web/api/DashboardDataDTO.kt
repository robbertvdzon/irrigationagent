package com.vdzon.irrigation.dashboard.internal.web.api

import com.vdzon.irrigation.advisory.RainHistory
import com.vdzon.irrigation.advisory.WeatherForecast
import com.vdzon.irrigation.irrigation.IrrigationAdvice
import com.vdzon.irrigation.irrigation.IrrigationEvent

data class DashboardDataDTO(
    val forecasts: List<WeatherForecast>,
    val history: List<RainHistory>,
    val advices: List<IrrigationAdvice>,
    val advice: IrrigationAdvice?,
    val events: List<IrrigationEvent>
)

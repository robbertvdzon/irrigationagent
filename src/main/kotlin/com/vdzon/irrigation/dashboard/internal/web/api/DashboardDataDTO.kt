package com.vdzon.irrigation.dashboard.internal.web.api

import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.irrigation.IrrigationEvent

data class DashboardDataDTO(
    val forecasts: List<WeatherForecast>,
    val history: List<RainHistory>,
    val advices: List<IrrigationAdvice>,
    val advice: IrrigationAdvice?,
    val events: List<IrrigationEvent>
)

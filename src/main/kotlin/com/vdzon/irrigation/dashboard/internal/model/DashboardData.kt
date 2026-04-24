package com.vdzon.irrigation.dashboard.internal.model

import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast

data class DashboardData(
    val forecasts: List<WeatherForecast>,
    val history: List<RainHistory>,
    val advices: List<IrrigationAdvice>,
    val advice: IrrigationAdvice?,
    val events: List<IrrigationEvent>
)
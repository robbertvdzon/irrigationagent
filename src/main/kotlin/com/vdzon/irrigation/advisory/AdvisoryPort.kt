package com.vdzon.irrigation.advisory

import java.time.LocalDate

interface AdvisoryPort {
    suspend fun calculateAndProposeAdvice(date: LocalDate)
    suspend fun getForecasts(): List<WeatherForecast>
    suspend fun getRainHistory(): List<RainHistory>
}

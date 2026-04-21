package com.vdzon.irrigation.advisory

import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastEntity
import java.time.LocalDate

interface AdvisoryPort {
    suspend fun calculateAndProposeAdvice(date: LocalDate)
    suspend fun getForecasts(): List<WeatherForecastEntity>
    suspend fun getRainHistory(): List<RainHistoryEntity>
}

package com.vdzon.irrigation.advisory

import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastEntity
import java.time.LocalDate

interface AdvisoryPort {
    fun calculateAndProposeAdvice(date: LocalDate)
    fun getForecasts(): List<WeatherForecastEntity>
    fun getRainHistory(): List<RainHistoryEntity>
}

package com.vdzon.irrigation.rainhistory

import java.time.LocalDate

data class RainHistory(
    val date: LocalDate,
    val rainMm: Double
)

interface RainHistoryPort {
    suspend fun updateRainHistory(days: Int): List<RainHistory>
    suspend fun getRainHistory(): List<RainHistory>
}

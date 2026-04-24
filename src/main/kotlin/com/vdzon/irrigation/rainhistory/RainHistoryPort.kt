package com.vdzon.irrigation.rainhistory

import java.time.LocalDate

/**
 * The public gateway for the rain history module.
 * Used by the advisory module to make decisions based on the past.
 */
interface RainHistoryPort {
    /**
     * Fetches the rain history for the specified number of days and refreshes the local database.
     */
    suspend fun updateRainHistory(days: Int): List<RainHistory>

    /**
     * Provides an overview of all saved rain data.
     */
    suspend fun getRainHistory(): List<RainHistory>
}

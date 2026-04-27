package com.vdzon.irrigation.rainhistory.internal

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.bind.annotation.RequestParam

data class RainHistoryResponse(
    val date: String,
    val rainMm: Double
)

@HttpExchange("/history")
interface RainHistoryClient {
    @GetExchange
    suspend fun getRainHistory(@RequestParam("days") days: Int): List<RainHistoryResponse>
}

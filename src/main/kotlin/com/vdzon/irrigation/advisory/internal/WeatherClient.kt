package com.vdzon.irrigation.advisory.internal

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

data class ForecastResponse(
    val date: String,
    val rainMm: Double
)

data class RainHistoryResponse(
    val date: String,
    val rainMm: Double
)

@FeignClient(name = "weatherClient", url = "\${weather.api.url}")
interface WeatherClient {
    @GetMapping("/forecast")
    fun getDailyForecast(): ForecastResponse

    @GetMapping("/history")
    fun getRainHistory(@RequestParam days: Int): List<RainHistoryResponse>
}

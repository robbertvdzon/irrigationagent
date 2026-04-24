package com.vdzon.irrigation.weatherforecast.internal

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

data class ForecastResponse(
    val date: String,
    val rainMm: Double,
    val maxTemp: Double
)

@Component
class WeatherForecastClient(
    @Value("\${weather.api.url}") private val baseUrl: String,
    webClientBuilder: WebClient.Builder
) {
    private val webClient by lazy { webClientBuilder.baseUrl(baseUrl).build() }

    suspend fun getDailyForecast(): ForecastResponse {
        return webClient.get()
            .uri("/forecast")
            .retrieve()
            .awaitBody<ForecastResponse>()
    }
}

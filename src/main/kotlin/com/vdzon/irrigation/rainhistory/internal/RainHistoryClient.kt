package com.vdzon.irrigation.rainhistory.internal

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

data class RainHistoryResponse(
    val date: String,
    val rainMm: Double
)

@Component
class RainHistoryClient(
    @Value("\${weather.api.url}") private val baseUrl: String,
    webClientBuilder: WebClient.Builder
) {
    private val webClient by lazy { webClientBuilder.baseUrl(baseUrl).build() }

    suspend fun getRainHistory(days: Int): List<RainHistoryResponse> {
        return webClient.get()
            .uri { it.path("/history").queryParam("days", days).build() }
            .retrieve()
            .awaitBody<List<RainHistoryResponse>>()
    }
}

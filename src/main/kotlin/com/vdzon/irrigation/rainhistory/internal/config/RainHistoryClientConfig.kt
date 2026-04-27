package com.vdzon.irrigation.rainhistory.internal.config

import com.vdzon.irrigation.rainhistory.internal.RainHistoryClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class RainHistoryClientConfig {
    @Bean
    fun rainHistoryClient(
        webClientBuilder: WebClient.Builder,
        @Value("\${weather.api.url}") baseUrl: String
    ): RainHistoryClient {
        val webClient = webClientBuilder.baseUrl(baseUrl).build()
        val adapter = WebClientAdapter.create(webClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(RainHistoryClient::class.java)
    }
}


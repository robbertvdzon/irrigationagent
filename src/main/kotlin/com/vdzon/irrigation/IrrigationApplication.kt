package com.vdzon.irrigation

import com.vdzon.irrigation.rainhistory.internal.RainHistoryClient
import com.vdzon.irrigation.weatherforecast.internal.WeatherForecastClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@SpringBootApplication
@EnableScheduling
class IrrigationApplication {

    @Bean
    fun r2dbcMappingContext(): R2dbcMappingContext {
        return R2dbcMappingContext.forPlainIdentifiers()
    }

    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

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

    @Bean
    fun weatherForecastClient(
        webClientBuilder: WebClient.Builder,
        @Value("\${weather.api.url}") baseUrl: String
    ): WeatherForecastClient {
        val webClient = webClientBuilder.baseUrl(baseUrl).build()
        val adapter = WebClientAdapter.create(webClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(WeatherForecastClient::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<IrrigationApplication>(*args)
}

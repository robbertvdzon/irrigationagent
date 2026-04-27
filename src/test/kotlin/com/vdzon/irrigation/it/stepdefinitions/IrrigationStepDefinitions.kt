package com.vdzon.irrigation.it.stepdefinitions

import com.vdzon.irrigation.it.CucumberSpringConfiguration
import com.vdzon.irrigation.weatherforecast.internal.persistence.WeatherForecastRepository
import com.vdzon.irrigation.rainhistory.internal.persistence.RainHistoryRepository
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventRepository
import com.github.tomakehurst.wiremock.client.WireMock
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

class IrrigationStepDefinitions {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var weatherForecastRepository: WeatherForecastRepository

    @Autowired
    private lateinit var rainHistoryRepository: RainHistoryRepository

    @Autowired
    private lateinit var irrigationAdviceRepository: IrrigationAdviceRepository

    @Autowired
    private lateinit var irrigationEventRepository: IrrigationEventRepository

    private val wireMock get() = CucumberSpringConfiguration.wireMock

    @Before
    fun setup() = runBlocking {
        weatherForecastRepository.deleteAll()
        rainHistoryRepository.deleteAll()
        irrigationAdviceRepository.deleteAll()
        irrigationEventRepository.deleteAll()
        wireMock.resetAll()
    }

    @Given("the weather forecast predicts {double} mm rain and {double} degrees")
    fun the_weather_forecast_predicts_mm_rain_and_degrees(rainMm: Double, maxTemp: Double) {
        val today = LocalDate.now().toString()
        wireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/forecast"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"date": "$today", "rainMm": $rainMm, "maxTemp": $maxTemp}""")))

        // Default also mock history to prevent nullpointers or empty history logic
        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/history"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[]")))
    }

    @Given("the weather forecast predicts {double} mm rain")
    fun the_weather_forecast_predicts_mm_rain(rainMm: Double) {
        the_weather_forecast_predicts_mm_rain_and_degrees(rainMm, 20.0) // default 20 degrees
    }

    @Given("the rain history of the past week is {double} mm")
    fun the_rain_history_of_the_past_week_is_mm(totalRain: Double) {
        // Mock a list of 7 days that together have totalRain mm rain
        val rainPerDay = totalRain / 7.0
        val historyJson = (1..7).map { day ->
            val date = LocalDate.now().minusDays(day.toLong()).toString()
            """{"date": "$date", "rainMm": $rainPerDay}"""
        }.joinToString(prefix = "[", postfix = "]", separator = ",")

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/history"))
            .willReturn(WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(historyJson)))
    }

    @Given("irrigation was executed yesterday")
    fun irrigation_was_executed_yesterday() = runBlocking {
        val yesterday = LocalDate.now().minusDays(1)
        irrigationEventRepository.save(
            com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity(
                eventDate = yesterday.atTime(8, 0),
                durationMinutes = 30,
                status = "COMPLETED"
            )
        )
    }

    @When("the daily advice is generated")
    fun the_daily_advice_is_generated() {
        webTestClient.post()
            .uri("/calculate-advice")
            .exchange()
            .expectStatus().isOk
        
        Thread.sleep(1000) // Wait for async event processing
    }

    @When("the daily advice is executed")
    fun the_daily_advice_is_executed() {
        webTestClient.post()
            .uri("/execute-now")
            .exchange()
            .expectStatus().isOk
    }

    @Then("the advice should be {int} minutes")
    fun the_advice_should_be_minutes(minutes: Int) = runBlocking {
        val advice = irrigationAdviceRepository.findByDate(LocalDate.now())
        assertNotNull(advice)
        assertEquals(minutes, advice?.durationMinutes)
    }

    @Then("the irrigation should have been executed")
    fun the_irrigation_should_have_been_executed() = runBlocking {
        val events = irrigationEventRepository.findAll().toList()
        assertTrue(events.any { it.status == "COMPLETED" }, "There should be a completed irrigation event")
    }

    @Then("the irrigation should not have been executed today")
    fun the_irrigation_should_not_have_been_executed_today() = runBlocking {
        val events = irrigationEventRepository.findAll().toList()
        assertTrue(events.none { it.eventDate.toLocalDate() == LocalDate.now() }, "There should be no irrigation event for today")
    }

    @Then("the irrigation should not have been executed")
    fun the_irrigation_should_not_have_been_executed() = runBlocking {
        val events = irrigationEventRepository.findAll().toList()
        assertTrue(events.none { it.status == "COMPLETED" && it.eventDate.toLocalDate() == LocalDate.now() }, "There should be no irrigation event for today")
    }
}

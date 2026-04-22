package com.vdzon.irrigation.it.stepdefinitions

import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryRepository
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastEntity
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventRepository
import com.jayway.jsonpath.JsonPath
import io.cucumber.java.Before
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DashboardApiStepDefinitions {

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

    private lateinit var response: WebTestClient.ResponseSpec

    @Before
    fun setup() = runBlocking {
        weatherForecastRepository.deleteAll()
        rainHistoryRepository.deleteAll()
        irrigationAdviceRepository.deleteAll()
        irrigationEventRepository.deleteAll()
    }

    @Given("the following forecasts:")
    fun the_following_forecasts(dataTable: DataTable) = runBlocking {
        val data = dataTable.asMaps(String::class.java, String::class.java)
        data.forEach { row ->
            val daysAgo = row["days_ago"]?.toLong() ?: 0L
            val rainMm = row["rain_mm"]?.toDouble() ?: 0.0
            weatherForecastRepository.save(
                WeatherForecastEntity(
                    forecastDate = LocalDate.now().minusDays(daysAgo),
                    rainExpectedMm = rainMm
                )
            )
        }
    }

    @Given("the following rain history:")
    fun the_following_rain_history(dataTable: DataTable) = runBlocking {
        val data = dataTable.asMaps(String::class.java, String::class.java)
        data.forEach { row ->
            val daysAgo = row["days_ago"]?.toLong() ?: 0L
            val rainMm = row["rain_mm"]?.toDouble() ?: 0.0
            rainHistoryRepository.save(
                RainHistoryEntity(
                    date = LocalDate.now().minusDays(daysAgo),
                    rainMm = rainMm
                )
            )
        }
    }

    @Given("the following irrigation advices:")
    fun the_following_irrigation_advices(dataTable: DataTable) = runBlocking {
        val data = dataTable.asMaps(String::class.java, String::class.java)
        data.forEach { row ->
            val daysAgo = row["days_ago"]?.toLong() ?: 0L
            val minutes = row["minutes"]?.toInt() ?: 0
            val status = row["status"] ?: "PENDING"
            irrigationAdviceRepository.save(
                IrrigationAdviceEntity(
                    date = LocalDate.now().minusDays(daysAgo),
                    durationMinutes = minutes,
                    status = status
                )
            )
        }
    }

    @Given("the following irrigation events:")
    fun the_following_irrigation_events(dataTable: DataTable) = runBlocking {
        val data = dataTable.asMaps(String::class.java, String::class.java)
        data.forEach { row ->
            val daysAgo = row["days_ago"]?.toLong() ?: 0L
            val minutes = row["minutes"]?.toInt() ?: 0
            val status = row["status"] ?: "COMPLETED"
            irrigationEventRepository.save(
                IrrigationEventEntity(
                    eventDate = LocalDateTime.of(LocalDate.now().minusDays(daysAgo), LocalTime.NOON),
                    durationMinutes = minutes,
                    status = status
                )
            )
        }
    }

    private var responseBody: ByteArray? = null

    @When("I call the dashboard data API")
    fun i_call_the_dashboard_data_api() {
        response = webTestClient.get()
            .uri("/api/dashboard/data")
            .exchange()
        responseBody = response.expectStatus().isOk
            .expectBody().returnResult().responseBody
    }

    @Then("the response should match:")
    fun the_response_should_match(dataTable: DataTable) {
        val json = String(responseBody!!)
        val expectations = dataTable.asMaps(String::class.java, String::class.java)
        
        expectations.forEach { row ->
            val path = row["path"]!!
            val expectedValue = row["value"]!!
            val actualValue = JsonPath.read<Any>(json, path).toString()
            assertEquals(expectedValue, actualValue, "Value at path $path did not match")
        }
    }
}

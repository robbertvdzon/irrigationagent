package com.vdzon.irrigation.it.stepdefinitions

import com.vdzon.irrigation.rainhistory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.rainhistory.internal.persistence.RainHistoryRepository
import com.vdzon.irrigation.weatherforecast.internal.persistence.WeatherForecastEntity
import com.vdzon.irrigation.weatherforecast.internal.persistence.WeatherForecastRepository
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceEntity
import com.vdzon.irrigation.advisory.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventEntity
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventRepository
import com.vdzon.irrigation.dashboard.internal.model.DashboardData
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
            val maxTemp = row["max_temp"]?.toDouble() ?: 20.0
            weatherForecastRepository.save(
                WeatherForecastEntity(
                    forecastDate = LocalDate.now().minusDays(daysAgo),
                    rainExpectedMm = rainMm,
                    maxTempCelsius = maxTemp
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
    private var dashboardData: DashboardData? = null

    @When("I call the dashboard data API")
    fun i_call_the_dashboard_data_api() {
        response = webTestClient.get()
            .uri("/api/dashboard/data")
            .exchange()
        val result = response.expectStatus().isOk
            .expectBody(DashboardData::class.java).returnResult()
        responseBody = result.responseBodyContent
        dashboardData = result.responseBody
    }

    @Then("the following forecasts is recieved in the response:")
    fun the_following_forecasts_is_recieved_in_the_response(dataTable: DataTable) {
        val expectedData = dataTable.asMaps(String::class.java, String::class.java)
        val actualForecasts = dashboardData?.forecasts ?: emptyList()

        assertEquals(expectedData.size, actualForecasts.size, "Number of forecasts did not match")

        expectedData.forEachIndexed { index, row ->
            val expectedDaysAgo = row["days_ago"]?.toLong() ?: 0L
            val expectedRainMm = row["rain_mm"]?.toDouble() ?: 0.0
            val expectedMaxTemp = row["max_temp"]?.toDouble() ?: 20.0
            val actualForecast = actualForecasts[index]

            val expectedDate = LocalDate.now().minusDays(expectedDaysAgo)
            assertEquals(expectedDate, actualForecast.date, "Date at index $index did not match")
            assertEquals(expectedRainMm, actualForecast.rainExpectedMm, 0.001, "Rain at index $index did not match")
            assertEquals(expectedMaxTemp, actualForecast.maxTempCelsius, 0.001, "Max temp at index $index did not match")
        }
    }

    @Then("the following rain history is recieved in the response:")
    fun the_following_rain_history_is_recieved_in_the_reponse(dataTable: DataTable) {
        val expectedData = dataTable.asMaps(String::class.java, String::class.java)
        val actualHistory = dashboardData?.history ?: emptyList()

        assertEquals(expectedData.size, actualHistory.size, "Number of history records did not match")

        expectedData.forEachIndexed { index, row ->
            val expectedDaysAgo = row["days_ago"]?.toLong() ?: 0L
            val expectedRainMm = row["rain_mm"]?.toDouble() ?: 0.0
            val actual = actualHistory[index]

            val expectedDate = LocalDate.now().minusDays(expectedDaysAgo)
            assertEquals(expectedDate, actual.date, "Date at index $index did not match")
            assertEquals(expectedRainMm, actual.rainMm, 0.001, "Rain at index $index did not match")
        }
    }

    @Then("the following advices is recieved in the response:")
    fun the_following_advices_is_recieved_in_the_response(dataTable: DataTable) {
        val expectedData = dataTable.asMaps(String::class.java, String::class.java)
        val actualAdvices = dashboardData?.advices ?: emptyList()

        assertEquals(expectedData.size, actualAdvices.size, "Number of advices did not match")

        expectedData.forEachIndexed { index, row ->
            val expectedDaysAgo = row["days_ago"]?.toLong() ?: 0L
            val expectedMinutes = row["minutes"]?.toInt() ?: 0
            val expectedStatus = row["status"] ?: "PENDING"
            val actual = actualAdvices[index]

            val expectedDate = LocalDate.now().minusDays(expectedDaysAgo)
            assertEquals(expectedDate, actual.date, "Date at index $index did not match")
            assertEquals(expectedMinutes, actual.durationMinutes, "Minutes at index $index did not match")
            assertEquals(expectedStatus, actual.status, "Status at index $index did not match")
        }
    }

    @Then("the following today advice is recieved in the response:")
    fun the_following_today_advice_is_recieved_in_the_response(dataTable: DataTable) {
        val expectedData = dataTable.asMaps(String::class.java, String::class.java)
        val actualAdvice = dashboardData?.advice

        if (expectedData.isEmpty()) {
            assertEquals(null, actualAdvice, "Expected no today advice but found one")
            return
        }

        val row = expectedData[0]
        val expectedMinutes = row["minutes"]?.toInt() ?: 0
        val expectedStatus = row["status"] ?: "PENDING"

        assertEquals(expectedMinutes, actualAdvice?.durationMinutes, "Today advice minutes did not match")
        assertEquals(expectedStatus, actualAdvice?.status, "Today advice status did not match")
    }

    @Then("the following events is recieved in the response:")
    fun the_following_events_is_recieved_in_the_response(dataTable: DataTable) {
        val expectedData = dataTable.asMaps(String::class.java, String::class.java)
        val actualEvents = dashboardData?.events ?: emptyList()

        assertEquals(expectedData.size, actualEvents.size, "Number of events did not match")

        expectedData.forEachIndexed { index, row ->
            val expectedDaysAgo = row["days_ago"]?.toLong() ?: 0L
            val expectedMinutes = row["minutes"]?.toInt() ?: 0
            val expectedStatus = row["status"] ?: "COMPLETED"
            val actual = actualEvents[index]

            val expectedDate = LocalDate.now().minusDays(expectedDaysAgo)
            assertEquals(expectedDate, actual.eventDate.toLocalDate(), "Date at index $index did not match")
            assertEquals(expectedMinutes, actual.durationMinutes, "Minutes at index $index did not match")
            assertEquals(expectedStatus, actual.status, "Status at index $index did not match")
        }
    }

}

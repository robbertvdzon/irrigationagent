package com.vdzon.irrigation.it.stepdefinitions

import com.vdzon.irrigation.dashboard.IrrigationAgent
import com.vdzon.irrigation.advisory.internal.persistence.WeatherForecastRepository
import com.vdzon.irrigation.advisory.internal.persistence.RainHistoryRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationAdviceRepository
import com.vdzon.irrigation.irrigation.internal.persistence.IrrigationEventRepository
import com.github.tomakehurst.wiremock.client.WireMock.*
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class IrrigationStepDefinitions {

    @Autowired
    private lateinit var agent: IrrigationAgent

    @Autowired
    private lateinit var weatherForecastRepository: WeatherForecastRepository

    @Autowired
    private lateinit var rainHistoryRepository: RainHistoryRepository

    @Autowired
    private lateinit var irrigationAdviceRepository: IrrigationAdviceRepository

    @Autowired
    private lateinit var irrigationEventRepository: IrrigationEventRepository

    @Before
    fun setup() {
        weatherForecastRepository.deleteAll()
        rainHistoryRepository.deleteAll()
        irrigationAdviceRepository.deleteAll()
        irrigationEventRepository.deleteAll()
        reset()
    }

    @Given("de weersverwachting voorspelt {double} mm regen")
    fun de_weersverwachting_voorspelt_mm_regen(rainMm: Double) {
        val today = LocalDate.now().toString()
        stubFor(get(urlEqualTo("/forecast"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"date": "$today", "rainMm": $rainMm}""")))
        
        // Standaard ook historie mocken om nullpointers of lege historie logic te voorkomen
        stubFor(get(urlPathEqualTo("/history"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[]")))
    }

    @Given("de regen historie van afgelopen week is {double} mm")
    fun de_regen_historie_van_afgelopen_week_is_mm(totalRain: Double) {
        // Mock een lijst van 7 dagen die samen totalRain mm regen hebben
        val rainPerDay = totalRain / 7.0
        val historyJson = (1..7).map { day ->
            val date = LocalDate.now().minusDays(day.toLong()).toString()
            """{"date": "$date", "rainMm": $rainPerDay}"""
        }.joinToString(prefix = "[", postfix = "]", separator = ",")

        stubFor(get(urlPathEqualTo("/history"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(historyJson)))
    }

    @When("het dagelijkse advies wordt gegenereerd")
    fun het_dagelijkse_advies_wordt_gegenereerd() {
        agent.generateDailyAdvice()
    }

    @When("het dagelijkse advies wordt uitgevoerd")
    fun het_dagelijkse_advies_wordt_uitgevoerd() {
        agent.executeDailyAdvice()
    }

    @Then("moet het advies {int} minuten zijn")
    fun moet_het_advies_minuten_zijn(minutes: Int) {
        val advice = irrigationAdviceRepository.findByDate(LocalDate.now())
        assertNotNull(advice)
        assertEquals(minutes, advice?.durationMinutes)
    }

    @Then("moet de irrigatie zijn uitgevoerd")
    fun moet_de_irrigatie_zijn_uitgevoerd() {
        val events = irrigationEventRepository.findAll()
        assertTrue(events.any { it.status == "COMPLETED" }, "Er zou een voltooid irrigatie event moeten zijn")
    }

    @Then("moet de irrigatie niet zijn uitgevoerd")
    fun moet_de_irrigatie_niet_zijn_uitgevoerd() {
        val events = irrigationEventRepository.findAll()
        assertTrue(events.none { it.eventDate.toLocalDate() == LocalDate.now() }, "Er zou geen irrigatie event moeten zijn voor vandaag")
    }
}

package com.vdzon.irrigation.advisory.internal.services

import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class AdvisoryCalculationServiceTest {

    private val service = AdvisoryCalculationService()
    private val today = LocalDate.now()

    @Test
    fun `should advise 30 minutes when it is hot and dry`() {
        // Arrange
        val history = listOf(RainHistory(today.minusDays(1), 0.0))
        val forecast = WeatherForecast(today, 0.0, 26.0) // Temp > 25
        val events = emptyList<IrrigationEvent>()

        // Act
        val result = service.calculateAndProposeAdvice(history, forecast, today, events)

        // Assert
        assertThat(result.durationMinutes).isEqualTo(30)
    }

    @Test
    fun `should advise 15 minutes when it is not hot but dry and not irrigated yesterday`() {
        // Arrange
        val history = listOf(RainHistory(today.minusDays(1), 0.0))
        val forecast = WeatherForecast(today, 0.0, 22.0) // Temp <= 25, Rain < 2.0
        val events = emptyList<IrrigationEvent>()

        // Act
        val result = service.calculateAndProposeAdvice(history, forecast, today, events)

        // Assert
        assertThat(result.durationMinutes).isEqualTo(15)
    }

    @Test
    fun `should advise 0 minutes when it is not hot but dry but already irrigated yesterday`() {
        // Arrange
        val history = listOf(RainHistory(today.minusDays(1), 0.0))
        val forecast = WeatherForecast(today, 0.0, 22.0)
        val events = listOf(IrrigationEvent(LocalDateTime.now().minusDays(1), 15, "COMPLETED"))

        // Act
        val result = service.calculateAndProposeAdvice(history, forecast, today, events)

        // Assert
        assertThat(result.durationMinutes).isEqualTo(0)
    }

    @Test
    fun `should advise 0 minutes when rain is expected today`() {
        // Arrange
        val history = listOf(RainHistory(today.minusDays(1), 0.0))
        val forecast = WeatherForecast(today, 3.0, 22.0) // Rain >= 2.0
        val events = emptyList<IrrigationEvent>()

        // Act
        val result = service.calculateAndProposeAdvice(history, forecast, today, events)

        // Assert
        assertThat(result.durationMinutes).isEqualTo(0)
    }

    @Test
    fun `should advise 0 minutes when there was enough rain last week`() {
        // Arrange
        val history = listOf(RainHistory(today.minusDays(1), 16.0)) // Total > 15.0
        val forecast = WeatherForecast(today, 0.0, 22.0)
        val events = emptyList<IrrigationEvent>()

        // Act
        val result = service.calculateAndProposeAdvice(history, forecast, today, events)

        // Assert
        assertThat(result.durationMinutes).isEqualTo(0)
    }
}

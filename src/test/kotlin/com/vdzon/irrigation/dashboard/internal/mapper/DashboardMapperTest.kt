package com.vdzon.irrigation.dashboard.internal.mapper

import com.vdzon.irrigation.dashboard.internal.model.DashboardData
import com.vdzon.irrigation.advisory.IrrigationAdvice
import com.vdzon.irrigation.irrigation.IrrigationEvent
import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.weatherforecast.WeatherForecast
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DashboardMapperTest {

    @Test
    fun `should map DashboardData to DashboardDataResponse`() {
        // Arrange
        val today = LocalDate.now()
        val now = LocalDateTime.now()
        
        val forecast = WeatherForecast(today, 2.5, 22.0)
        val rainHistory = RainHistory(today.minusDays(1), 1.0)
        val advice = IrrigationAdvice(today, 15, "PENDING")
        val event = IrrigationEvent(now, 15, "COMPLETED")
        
        val dashboardData = DashboardData(
            forecasts = listOf(forecast),
            history = listOf(rainHistory),
            advices = listOf(advice),
            advice = advice,
            events = listOf(event)
        )

        // Act
        val response = dashboardData.toResponse()

        // Assert
        assertThat(response.forecasts).hasSize(1)
        assertThat(response.forecasts[0].date).isEqualTo(today)
        assertThat(response.forecasts[0].rainExpectedMm).isEqualTo(2.5)
        assertThat(response.forecasts[0].maxTempCelsius).isEqualTo(22.0)

        assertThat(response.history).hasSize(1)
        assertThat(response.history[0].date).isEqualTo(today.minusDays(1))
        assertThat(response.history[0].rainMm).isEqualTo(1.0)

        assertThat(response.advices).hasSize(1)
        assertThat(response.advices[0].date).isEqualTo(today)
        assertThat(response.advices[0].durationMinutes).isEqualTo(15)
        assertThat(response.advices[0].status).isEqualTo("PENDING")

        assertThat(response.advice?.date).isEqualTo(today)
        assertThat(response.advice?.durationMinutes).isEqualTo(15)

        assertThat(response.events).hasSize(1)
        assertThat(response.events[0].eventDate).isEqualTo(now)
        assertThat(response.events[0].durationMinutes).isEqualTo(15)
        assertThat(response.events[0].status).isEqualTo("COMPLETED")
    }

    @Test
    fun `should handle null today advice`() {
        // Arrange
        val dashboardData = DashboardData(
            forecasts = emptyList(),
            history = emptyList(),
            advices = emptyList(),
            advice = null,
            events = emptyList()
        )

        // Act
        val response = dashboardData.toResponse()

        // Assert
        assertThat(response.advice).isNull()
    }
}

package com.vdzon.irrigation.dashboard.internal.service

import com.vdzon.irrigation.dashboard.internal.model.DashboardDataDTO
import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import com.vdzon.irrigation.rainhistory.RainHistoryPort
import com.vdzon.irrigation.weatherforecast.WeatherForecastPort
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val weatherForecastPort: WeatherForecastPort,
    private val rainHistoryPort: RainHistoryPort,
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort
) {

    suspend fun getDashboardData(): DashboardDataDTO = coroutineScope {
        // We start all queries directly (parallel)
        val forecastsDeferred = async { weatherForecastPort.getAllForecasts() }
        val historyDeferred = async { rainHistoryPort.getRainHistory() }
        val advicesDeferred = async { advisoryPort.getAdvices() }
        val todayAdviceDeferred = async { advisoryPort.getTodayAdvice() }
        val eventsDeferred = async { irrigationPort.getEvents() }

        // We wait for the results and build the DTO
        DashboardDataDTO(
            forecasts = forecastsDeferred.await(),
            history = historyDeferred.await(),
            advices = advicesDeferred.await(),
            advice = todayAdviceDeferred.await(),
            events = eventsDeferred.await()
        )
    }
}

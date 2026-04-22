package com.vdzon.irrigation.dashboard.internal.web.api

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController(
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort
) {
    @GetMapping("/data")
    suspend fun getDashboardData(): DashboardDataDTO = coroutineScope {
        // We starten alle queries direct (parallel)
        val forecastsDeferred = async { advisoryPort.getForecasts() }
        val historyDeferred = async { advisoryPort.getRainHistory() }
        val advicesDeferred = async { irrigationPort.getAdvices() }
        val todayAdviceDeferred = async { irrigationPort.getTodayAdvice() }
        val eventsDeferred = async { irrigationPort.getEvents() }

        // We wachten op de resultaten en bouwen de DTO
        DashboardDataDTO(
            forecasts = forecastsDeferred.await(),
            history = historyDeferred.await(),
            advices = advicesDeferred.await(),
            advice = todayAdviceDeferred.await(),
            events = eventsDeferred.await()
        )
    }
}

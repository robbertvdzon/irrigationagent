package com.vdzon.irrigation.dashboard.internal.dashboard.controller

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.dashboard.internal.dashboard.service.DashboardService
import com.vdzon.irrigation.irrigation.IrrigationPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class DashboardController(
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort,
    private val dashboardService: DashboardService
) {

    private val logger = LoggerFactory.getLogger(DashboardController::class.java)

    @GetMapping("/")
    suspend fun index(model: Model): String {
        logger.info("Serving index page")
        val data = dashboardService.getDashboardData()
        model.addAttribute("forecasts", data.forecasts)
        model.addAttribute("history", data.history)
        model.addAttribute("advices", data.advices)
        model.addAttribute("advice", data.advice)
        model.addAttribute("events", data.events)
        return "index"
    }

    @GetMapping("/advice-fragment")
    suspend fun getAdviceFragment(model: Model): String {
        model.addAttribute("advice", advisoryPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/calculate-advice")
    suspend fun calculateAdvice(model: Model): String {
        advisoryPort.calculateAndProposeAdvice(LocalDate.now())
        // Note: In an async event-driven system, the advice might not be there IMMEDIATELY.
        // For now, we assume it's fast or the UI will refresh.
        model.addAttribute("advice", advisoryPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PutMapping("/update-advice")
    suspend fun updateAdvice(@RequestParam minutes: Int, model: Model): String {
        advisoryPort.saveAdvice(LocalDate.now(), minutes, "MANUALLY_ADJUSTED")
        model.addAttribute("advice", advisoryPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/execute-now")
    suspend fun executeNow(model: Model): String {
        val todayAdvice = advisoryPort.getTodayAdvice()
        if (todayAdvice != null && todayAdvice.durationMinutes > 0) {
            irrigationPort.startIrrigation(todayAdvice.durationMinutes)
            advisoryPort.saveAdvice(todayAdvice.date, todayAdvice.durationMinutes, "EXECUTED")
        }
        model.addAttribute("advice", advisoryPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }
}
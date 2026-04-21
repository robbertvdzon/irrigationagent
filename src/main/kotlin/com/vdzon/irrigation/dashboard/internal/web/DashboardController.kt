package com.vdzon.irrigation.dashboard.internal.web

import com.vdzon.irrigation.advisory.AdvisoryPort
import com.vdzon.irrigation.irrigation.IrrigationPort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
class DashboardController(
    private val advisoryPort: AdvisoryPort,
    private val irrigationPort: IrrigationPort
) {

    private val logger = org.slf4j.LoggerFactory.getLogger(DashboardController::class.java)

    @GetMapping("/")
    fun index(model: Model): String {
        logger.info("Serving index page")
        populateModel(model)
        return "index"
    }

    @GetMapping("/advice-fragment")
    fun getAdviceFragment(model: Model): String {
        model.addAttribute("advice", irrigationPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/calculate-advice")
    fun calculateAdvice(model: Model): String {
        advisoryPort.calculateAndProposeAdvice(LocalDate.now())
        // Note: In an async event-driven system, the advice might not be there IMMEDIATELY.
        // For now, we assume it's fast or the UI will refresh.
        model.addAttribute("advice", irrigationPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PutMapping("/update-advice")
    fun updateAdvice(@RequestParam minutes: Int, model: Model): String {
        irrigationPort.saveAdvice(LocalDate.now(), minutes, "MANUALLY_ADJUSTED")
        model.addAttribute("advice", irrigationPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/execute-now")
    fun executeNow(model: Model): String {
        irrigationPort.executeAdvice(LocalDate.now())
        model.addAttribute("advice", irrigationPort.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    private fun populateModel(model: Model) {
        model.addAttribute("forecasts", advisoryPort.getForecasts())
        model.addAttribute("history", advisoryPort.getRainHistory())
        model.addAttribute("advices", irrigationPort.getAdvices())
        model.addAttribute("advice", irrigationPort.getTodayAdvice())
        model.addAttribute("events", irrigationPort.getEvents())
    }
}

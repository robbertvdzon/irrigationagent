package com.vdzon.irrigation.dashboard

import com.vdzon.irrigation.advisory.AdvisoryService
import com.vdzon.irrigation.irrigation.IrrigationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
class DashboardController(
    private val advisoryService: AdvisoryService,
    private val irrigationService: IrrigationService
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
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/calculate-advice")
    fun calculateAdvice(model: Model): String {
        advisoryService.calculateAndProposeAdvice(LocalDate.now())
        // Note: In an async event-driven system, the advice might not be there IMMEDIATELY.
        // For now, we assume it's fast or the UI will refresh.
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PutMapping("/update-advice")
    fun updateAdvice(@RequestParam minutes: Int, model: Model): String {
        irrigationService.saveAdvice(LocalDate.now(), minutes, "MANUALLY_ADJUSTED")
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PostMapping("/execute-now")
    fun executeNow(model: Model): String {
        irrigationService.executeAdvice(LocalDate.now())
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    private fun populateModel(model: Model) {
        model.addAttribute("forecasts", advisoryService.getForecasts())
        model.addAttribute("history", advisoryService.getRainHistory())
        model.addAttribute("advices", irrigationService.getAdvices())
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        model.addAttribute("events", irrigationService.getEvents())
    }
}

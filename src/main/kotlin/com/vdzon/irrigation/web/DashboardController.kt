package com.vdzon.irrigation.web

import com.vdzon.irrigation.service.IrrigationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
@RequestMapping("/")
class DashboardController(private val irrigationService: IrrigationService) {

    @GetMapping
    fun index(model: Model): String {
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
        irrigationService.calculateAndSaveAdvice(LocalDate.now())
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        return "fragments/advice :: advice-section"
    }

    @PutMapping("/update-advice")
    fun updateAdvice(@RequestParam minutes: Int, model: Model): String {
        irrigationService.updateAdvice(LocalDate.now(), minutes)
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
        model.addAttribute("forecasts", irrigationService.getForecasts())
        model.addAttribute("history", irrigationService.getRainHistory())
        model.addAttribute("advices", irrigationService.getAdvices())
        model.addAttribute("advice", irrigationService.getTodayAdvice())
        model.addAttribute("events", irrigationService.getEvents())
    }
}

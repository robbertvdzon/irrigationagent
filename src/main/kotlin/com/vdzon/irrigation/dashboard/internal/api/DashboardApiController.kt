package com.vdzon.irrigation.dashboard.internal.api

import com.vdzon.irrigation.dashboard.internal.service.DashboardService
import com.vdzon.irrigation.dashboard.internal.mapper.toResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController(
    private val dashboardService: DashboardService
) {
    @GetMapping("/data")
    suspend fun getDashboardData(): DashboardDataResponse = dashboardService.getDashboardData().toResponse()
}

package com.vdzon.irrigation.dashboard.internal.web.api

import com.vdzon.irrigation.dashboard.DashboardPort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController(
    private val dashboardPort: DashboardPort
) {
    @GetMapping("/data")
    suspend fun getDashboardData(): DashboardDataDTO = dashboardPort.getDashboardData()
}

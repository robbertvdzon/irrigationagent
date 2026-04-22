package com.vdzon.irrigation.dashboard

import com.vdzon.irrigation.dashboard.internal.web.api.DashboardDataDTO

interface DashboardPort {
    suspend fun getDashboardData(): DashboardDataDTO
}

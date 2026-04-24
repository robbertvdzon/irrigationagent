package com.vdzon.irrigation.rainhistory.internal

import com.vdzon.irrigation.rainhistory.RainHistory
import com.vdzon.irrigation.rainhistory.RainHistoryPort
import com.vdzon.irrigation.rainhistory.internal.persistence.RainHistoryEntity
import com.vdzon.irrigation.rainhistory.internal.persistence.RainHistoryRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class RainHistoryService(
    private val rainHistoryClient: RainHistoryClient,
    private val rainHistoryRepository: RainHistoryRepository
) : RainHistoryPort {

    override suspend fun updateRainHistory(days: Int): List<RainHistory> {
        val historyResponse = rainHistoryClient.getRainHistory(days)
        val entities = historyResponse.map {
            RainHistoryEntity(
                date = LocalDate.parse(it.date),
                rainMm = it.rainMm
            )
        }
        entities.forEach { rainHistoryRepository.save(it) }
        return entities.map { RainHistory(it.date, it.rainMm) }
    }

    override suspend fun getRainHistory(): List<RainHistory> {
        return rainHistoryRepository.findAllByOrderByDateDesc()
            .toList()
            .map { RainHistory(it.date, it.rainMm) }
    }
}

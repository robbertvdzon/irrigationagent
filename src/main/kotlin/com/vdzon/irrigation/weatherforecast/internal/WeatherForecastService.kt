package com.vdzon.irrigation.weatherforecast.internal

import com.vdzon.irrigation.weatherforecast.WeatherForecast
import com.vdzon.irrigation.weatherforecast.WeatherForecastPort
import com.vdzon.irrigation.weatherforecast.internal.persistence.WeatherForecastEntity
import com.vdzon.irrigation.weatherforecast.internal.persistence.WeatherForecastRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WeatherForecastService(
    private val weatherForecastClient: WeatherForecastClient,
    private val weatherForecastRepository: WeatherForecastRepository
) : WeatherForecastPort {

    override suspend fun getForecast(date: LocalDate): WeatherForecast {
        val forecastResponse = weatherForecastClient.getDailyForecast()
        val entity = WeatherForecastEntity(
            forecastDate = LocalDate.parse(forecastResponse.date),
            rainExpectedMm = forecastResponse.rainMm
        )
        weatherForecastRepository.save(entity)
        return WeatherForecast(entity.forecastDate, entity.rainExpectedMm)
    }

    override suspend fun getAllForecasts(): List<WeatherForecast> {
        return weatherForecastRepository.findAllByOrderByForecastDateDesc()
            .toList()
            .map { WeatherForecast(it.forecastDate, it.rainExpectedMm) }
    }
}

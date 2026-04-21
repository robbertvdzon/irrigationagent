package com.vdzon.irrigation.it

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import com.vdzon.irrigation.IrrigationApplication
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock

@CucumberContextConfiguration
@SpringBootTest(classes = [IrrigationApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ContextConfiguration(initializers = [CucumberSpringConfiguration.Initializer::class])
class CucumberSpringConfiguration {

    class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            postgres.start()
            TestPropertyValues.of(
                "spring.datasource.url=${postgres.jdbcUrl}",
                "spring.datasource.username=${postgres.username}",
                "spring.datasource.password=${postgres.password}",
                "weather.api.url=http://localhost:\${wiremock.server.port}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }

    companion object {
        val postgres = PostgreSQLContainer<Nothing>("postgres:16-alpine").apply {
            withDatabaseName("irrigation")
            withUsername("postgres")
            withPassword("postgres")
        }
    }
}

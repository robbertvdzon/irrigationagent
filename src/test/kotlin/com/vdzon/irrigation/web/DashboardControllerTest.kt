package com.vdzon.irrigation.dashboard.internal.web

import com.vdzon.irrigation.it.CucumberSpringConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = [CucumberSpringConfiguration.Initializer::class])
class DashboardControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `test index page returns 200`() {
        webTestClient.get().uri("/")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType("text/html;charset=UTF-8")
    }

    @Test
    fun `test index page with htmx fragment returns 200`() {
        webTestClient.get().uri("/advice-fragment")
            .exchange()
            .expectStatus().isOk
    }
}

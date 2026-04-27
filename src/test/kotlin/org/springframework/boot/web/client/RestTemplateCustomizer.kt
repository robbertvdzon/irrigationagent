package org.springframework.boot.web.client

import org.springframework.web.client.RestTemplate

/*
TODO: CHECK WHY THIS CLASS IS NEEDED FOR THE IT TESTS?
 */

fun interface RestTemplateCustomizer {
    fun customize(restTemplate: RestTemplate)
}

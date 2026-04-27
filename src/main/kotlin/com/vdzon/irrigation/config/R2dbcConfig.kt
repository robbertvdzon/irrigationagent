package com.vdzon.irrigation.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext

@Configuration
class R2dbcConfig {

    @Bean
    fun r2dbcMappingContext(): R2dbcMappingContext =
        R2dbcMappingContext.forPlainIdentifiers()
}

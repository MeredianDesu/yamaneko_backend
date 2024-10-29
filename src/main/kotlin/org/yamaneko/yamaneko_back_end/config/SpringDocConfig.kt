package org.yamaneko.yamaneko_back_end.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SpringDocConfig {
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("yamaneko-api")
            .pathsToMatch("/api/**")
            .build()
    }
}
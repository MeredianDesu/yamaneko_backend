package org.yamaneko.yamaneko_back_end.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
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

    @Bean
    fun apiInfo(): io.swagger.v3.oas.models.OpenAPI {
        return io.swagger.v3.oas.models.OpenAPI()
            .info(
                Info()
                    .title("Yamaneko API")
                    .description("API для онлайн-кинотеатра Yamaneko, позволяющий пользователям просматривать и взаимодействовать с аниме-контентом.")
                    .version("1.0.0")
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://github.com/MeredianDesu/yamaneko_backend?tab=MIT-1-ov-file")
                    )
            )
    }
}
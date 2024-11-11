package org.yamaneko.yamaneko_back_end.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
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
    fun openApi(): OpenAPI {
        return OpenAPI()
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
            .components(
                Components().addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT") // Specifies that it's a JWT token
                )
            )
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth")
            )
    }
}
package org.yamaneko.yamaneko_back_end.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {
    override fun addCorsMappings( registry: CorsRegistry ) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://yamaneko.isn.one", "http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE" )
            .allowedHeaders("*")
            .allowCredentials( true )
    }
}
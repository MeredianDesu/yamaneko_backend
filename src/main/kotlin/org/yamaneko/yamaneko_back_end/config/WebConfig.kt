package org.yamaneko.yamaneko_back_end.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addCorsMappings( registry: CorsRegistry ) {
        registry.addMapping("/**")
            .allowedOrigins("https://yamaneko.isn.one", "http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE" )
            .allowedHeaders("*")
            .allowCredentials( true )
    }
}
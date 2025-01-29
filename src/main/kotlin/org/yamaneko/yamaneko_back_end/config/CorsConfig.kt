package org.yamaneko.yamaneko_back_end.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@Configuration
//@EnableWebMvc
//class CorsConfig: WebMvcConfigurer {
//    override fun addCorsMappings( registry: CorsRegistry ) {
//        registry.addMapping("/api/**")
//            .allowedOrigins("https://yamaneko.isn.one", "http://localhost:5173/", "http://localhost:8080/", "https://admin.yamaneko.isn.one/")
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS" )
//            .allowedHeaders("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Content-Type", "X-Requested-With")
//            .allowCredentials( true )
//        println("CORS configuration APPLIED")
//    }
//}
package org.yamaneko.yamaneko_back_end.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.yamaneko.yamaneko_back_end.config.filter.JwtAuthenticationFilter
import org.yamaneko.yamaneko_back_end.service.user_details.CustomUserDetailsService
import org.yamaneko.yamaneko_back_end.utils.PasswordSecurity

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val passwordSecurity: PasswordSecurity
){
    @Bean
    fun securityFilterChain( http: HttpSecurity ): SecurityFilterChain {
        http.csrf{ csrf ->
            csrf
                .disable()
        }
//            .cors {
//                it.configurationSource(corsConfigurationSource())
//            }
            .formLogin{
                formLogin ->
                    formLogin.loginPage("/login")
                    formLogin.defaultSuccessUrl( "/docs", true )
                    formLogin.permitAll()
            }
            .authorizeHttpRequests{ authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers(
                        HttpMethod.OPTIONS, "/api/**" // добавление этого параметра зафиксило cors на React
                    ).permitAll()
                    .requestMatchers(
                        "/login",                // Доступ к странице логина
                        "/swagger-ui/**",        // Swagger UI
                        "/v3/api-docs/**",       // OpenAPI спецификации
                        "/swagger-resources/**", // Ресурсы Swagger
                        "/webjars/**",           // Webjars (CSS/JS)
                        "/docs"                  // Ваш кастомный маршрут для Swagger
                    ).permitAll()
                    .requestMatchers(
                        "/api/auth/**",
                        "/files/"
                    ).permitAll()
                    authorizeHttpRequests
                        .requestMatchers(
                            "/api/users/**",
                            "/api/releases/**",
                            "/api/news/**",
                            "/api/genres/**",
                            "/api/banners/**",
                            "/api/characters/**",
                            "/api/team/**"
                        )
                        .hasAuthority("ADMIN")
            }
            .exceptionHandling{ exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint{ request, response, authException ->
                        val contentTypeHeader = request.getHeader("Accept")
                        if( request.requestURI.startsWith( "/login" ) ) {
                            response.sendRedirect( "/login" )
                        }
                        if( contentTypeHeader != null && contentTypeHeader.contains("application/json") ) {
                            response.contentType = "application/json"
                            response.status = HttpServletResponse.SC_UNAUTHORIZED
                            response.writer.write(
                                """{"error": "Unauthorized", "message": "${authException.message}"}"""
                            )
                        } else {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED )
                        }
                    }
            }
            .sessionManagement{ sessionManagement ->
                sessionManagement
                    .sessionConcurrency{ sessionConcurrency ->
                        sessionConcurrency
                            .maximumSessions(1)
                            .expiredUrl( "/login?expired" )
                    }
            }
            .addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java )

        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {

        return JwtAuthenticationFilter()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {

        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager( http: HttpSecurity ): AuthenticationManager {
        // Используем getSharedObject для получения AuthenticationManagerBuilder
        val authenticationManagerBuilder = http.getSharedObject( AuthenticationManagerBuilder::class.java )
        authenticationManagerBuilder.authenticationProvider( daoAuthenticationProvider() )

        return authenticationManagerBuilder.build()
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService( customUserDetailsService )
        provider.setPasswordEncoder( object : PasswordEncoder {

            override fun encode( rawPassword: CharSequence? ): String {
                return try {
                    throw UnsupportedOperationException("Encoding is not supported.")
                } catch ( e: UnsupportedOperationException ){
                    println( "Password encoding exception: $e" )
                }.toString()
            }

            override fun matches( rawPassword: CharSequence?, encodedPassword: String? ): Boolean {
                // Используем ваш сервис для проверки пароля
                return passwordSecurity.verifyPassword( rawPassword.toString(), encodedPassword.toString() )
            }
        })

        return provider
    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = listOf("https://yamaneko.isn.one", "http://localhost:5173/", "http://localhost:8080/", "https://admin.yamaneko.isn.one/")
//        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//        configuration.allowedHeaders = listOf("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Content-Type", "X-Requested-With")
//        configuration.allowCredentials = true
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//
//        return source
//    }
}
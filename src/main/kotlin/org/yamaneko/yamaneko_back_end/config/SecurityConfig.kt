package org.yamaneko.yamaneko_back_end.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.yamaneko.yamaneko_back_end.config.filter.JwtAuthenticationFilter
import org.yamaneko.yamaneko_back_end.entity.Role

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
  
  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http.csrf { it.disable() }
      .cors { it.configurationSource(corsConfigurationSource()) }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests { authorizeRequests ->
        authorizeRequests.requestMatchers(HttpMethod.OPTIONS, "/api/**")
          .permitAll() // CORS preflight для React
          .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**", "/docs")
          .permitAll()
          .requestMatchers("/api/auth/**", "/api/users/v1/user")
          .permitAll()
          .requestMatchers(
            HttpMethod.GET,
            "/api/releases/**",
            "/api/achievements/**",
            "/api/users/**",
            "/api/files/v2/user-file-upload",
            "/api/post/v1/{id}",
            "/api/team/**"
          )
          .permitAll()
          .requestMatchers("/api/users/v1/watchlist/**")
          .hasAnyAuthority(
            Role.ROLE_USER.name, Role.ROLE_ADMIN.name
          )
          .requestMatchers(
            "/api/users/**", "/api/banners/**", "/api/characters/**", "/api/genres/**", "/api/news/**",
//            "/api/files/**",
            "/api/achievements/**"
          )
          .hasAuthority(Role.ROLE_USER.name)
          .requestMatchers(HttpMethod.PATCH, "/api/users/v1/{username}")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.POST, "/api/releases/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.PATCH, "/api/releases/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.DELETE, "/api/releases/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.POST, "/api/users/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.PATCH, "/api/users/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.DELETE, "/api/users/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.POST, "/api/achievements/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.PATCH, "/api/achievements/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.DELETE, "/api/achievements/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.POST, "/api/genres/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.PATCH, "/api/genres/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.DELETE, "/api/genres/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.POST, "/api/team/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.PATCH, "/api/team/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .requestMatchers(HttpMethod.DELETE, "/api/team/**")
          .hasAuthority(Role.ROLE_ADMIN.name)
          .anyRequest()
          .authenticated() // Остальные запросы требуют аутентификации
      }
      .exceptionHandling {
        it.authenticationEntryPoint { request, response, _ ->
          if(request.getHeader("Authorization")
              .isNullOrBlank()
          ) {
            response.contentType = "application/json"
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("""{"error": "Unauthorized", "message": "Access denied"}""")
          }
        }
        it.accessDeniedHandler(accessDeniedHandler())
      }
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    
    return http.build()
  }
  
  @Bean
  fun accessDeniedHandler(): AccessDeniedHandler {
    return AccessDeniedHandler { _, response, _ ->
      response.contentType = "application/json"
      response.status = HttpServletResponse.SC_FORBIDDEN
      response.writer.write("""{"error": "Forbidden", "message": "You do not have permissions to access this resource"}""")
    }
  }
  
  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf(
      "https://yamaneko.isn.one", "http://localhost:5173", "http://localhost:8080", "https://admin.yamaneko.isn.one"
    )
    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
    configuration.allowedHeaders = listOf(
      "Authorization", "Content-Type", "X-Requested-With"
    )
    configuration.allowCredentials = true
    
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    
    return source
  }
}

package org.yamaneko.yamaneko_back_end.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.yamaneko.yamaneko_back_end.config.filter.JwtAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    // Master user
    @Value("\${master.name}")
    private var masterUsername: String = ""
    @Value("\${master.password}")
    private var masterPassword: String = ""

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests()
//                .requestMatchers("/api/v1/users/register", "/api/v1/users/login", "/api/v1/releases", "/api/v1/users/refresh").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
            .and()
            .httpBasic()
            .and()
//            .addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java )

        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user: UserDetails = User.builder()
            .username( masterUsername )
            .password(passwordEncoder().encode( masterPassword ) )
            .roles("DEV")
            .build()

        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    //    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .csrf().disable()
//            .authorizeHttpRequests()
//            .requestMatchers("/**").hasRole("USER")
//            .anyRequest().authenticated()
//            .and()
//            .formLogin().disable()
//            .httpBasic().disable()
//            .addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java )
//
//        println("Security filter chain")
//        return http.build()
//    }
}
package org.yamaneko.yamaneko_back_end.config

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
import org.yamaneko.yamaneko_back_end.config.filter.JwtAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain( http: HttpSecurity ): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/v1/users/register").permitAll()
                .requestMatchers("/api/v1/users/login").permitAll()
                .requestMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
            .and()
            .httpBasic()

        println("Security filter chain")
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user: UserDetails = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password")) // Make sure the password is encoded
            .roles("USER")
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
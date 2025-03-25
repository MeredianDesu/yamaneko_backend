package org.yamaneko.yamaneko_back_end.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.yamaneko.yamaneko_back_end.service.user_details.CustomUserDetailsService
import org.yamaneko.yamaneko_back_end.utils.JwtUtil

@Component
class JwtAuthenticationFilter: OncePerRequestFilter() {
  @Autowired
  private lateinit var jwtUtil: JwtUtil
  
  @Autowired
  private lateinit var customUserDetailsService: CustomUserDetailsService
  
  @Throws(ServletException::class)
  override fun doFilterInternal(
    request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val authHeader = request.getHeader("Authorization")
    
    // Если заголовка нет или он не начинается с Bearer, просто продолжаем цепочку фильтров
    if(authHeader.isNullOrBlank() || ! authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }
    
    val jwt = authHeader.removePrefix("Bearer ").trim()
    
    request.setAttribute("AuthorizationToken", jwt)
    
    val id: String? = try {
      jwtUtil.extractUserId(jwt)
    } catch(e: Exception) {
      filterChain.doFilter(request, response)
      return
    }
    
    if(id == null || SecurityContextHolder.getContext().authentication != null) {
      filterChain.doFilter(request, response)
      return
    }
    
    // Если токен невалиден, прерываем обработку
    if(! jwtUtil.validateToken(jwt, id)) {
      filterChain.doFilter(request, response)
      return
    }
    
    val userDetails = customUserDetailsService.loadUserByUsername(id) ?: run {
      filterChain.doFilter(request, response)
      return
    }
    
    val authentication = UsernamePasswordAuthenticationToken(
      userDetails, null, userDetails.authorities
    )
    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
    SecurityContextHolder.getContext().authentication = authentication
    
    filterChain.doFilter(request, response)
  }
}

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
import org.yamaneko.yamaneko_back_end.customExceptions.JwtSignatureException
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
    request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
  ) {
//    val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    
    if(request.requestURI.startsWith("/api/auth/**")) {
      filterChain.doFilter(request, response)
      return
    }
    
    val authHeader = request.getHeader("Authorization")
    
    // Если заголовка нет или он не начинается с Bearer, просто продолжаем цепочку фильтров
    if(authHeader.isNullOrBlank() || ! authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }
    
    val jwt = authHeader.removePrefix("Bearer ").trim()
    
    request.setAttribute("AuthorizationToken", jwt)
    
    try {
      // Попытка извлечь ID пользователя и выполнить валидацию токена
      val id: String? = jwtUtil.extractUserId(jwt)
      
      if(id == null || SecurityContextHolder.getContext().authentication != null) {
        filterChain.doFilter(request, response)
        return
      }
      
      // Если токен невалиден, прерываем обработку
      if(! jwtUtil.validateToken(jwt, id)) {
        filterChain.doFilter(request, response)
        return
      }
      
      val userDetails = customUserDetailsService.loadUserByUsername(id)
      
      val authentication = UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.authorities
      )
      
      authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
      SecurityContextHolder.getContext().authentication = authentication
      
    } catch(e: JwtSignatureException) {
      // В случае ошибки подписи JWT, возвращаем ошибку 403
      response.status = HttpServletResponse.SC_FORBIDDEN
      response.contentType = "application/json"
      response.writer.write("""{"error": "Forbidden", "message": "Invalid JWT signature"}""")
      return
    } catch(e: Exception) {
      // В случае других ошибок токена
      filterChain.doFilter(request, response)
      return
    }
    
    filterChain.doFilter(request, response)
  }
}

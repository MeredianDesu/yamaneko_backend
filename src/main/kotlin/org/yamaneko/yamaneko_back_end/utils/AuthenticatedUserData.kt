package org.yamaneko.yamaneko_back_end.utils

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder

class AuthenticatedUserData {
  
  val logger = LoggerFactory.getLogger(javaClass)
  fun getAuthenticatedUser(): String? {
    val authentication = SecurityContextHolder.getContext().authentication
    logger.info("Authentication: {}", authentication.toString())
    return authentication?.name
  }
  
  fun getClientIpAddress(request: HttpServletRequest): String {
    val forwardedFor = request.getHeader("X-Forwarded-For")
    return if(forwardedFor != null && forwardedFor.isNotEmpty()) {
      forwardedFor.split(",")[0]
    } else {
      request.remoteAddr
    }
  }
}
package org.yamaneko.yamaneko_back_end.utils

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder

class AuthenticatedUserData {
    fun getAuthenticatedUser(): String?{
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.name
    }
    fun getClientIpAddress(request: HttpServletRequest): String {
        val forwardedFor = request.getHeader("X-Forwarded-For")
        return if (forwardedFor != null && forwardedFor.isNotEmpty()) {
            forwardedFor.split(",")[0]
        } else {
            request.remoteAddr
        }
    }
}
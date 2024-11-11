package org.yamaneko.yamaneko_back_end.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import org.yamaneko.yamaneko_back_end.utils.JwtUtil

class JwtAuthenticationFilter : OncePerRequestFilter() {

    private val jwtUtil: JwtUtil = JwtUtil()

    @Throws(ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain ) {
        val authHeader = request.getHeader("Authorization")
        var id: String? = null
        var jwt: String? = null

        if ( authHeader != null && authHeader.startsWith("Bearer ") ) {
            jwt = authHeader.substring(7)
            id = jwtUtil.extractUserId( jwt )
        }

        // Ensure JWT is not null and username is extracted
        if ( id != null && jwt != null && SecurityContextHolder.getContext().authentication == null) {
            if ( jwtUtil.validateToken(jwt, id)) {
                val authentication = UsernamePasswordAuthenticationToken(id, null, emptyList())
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        request.setAttribute( "AuthorizationToken", jwt )
        println( "Filter: JWT: $jwt" )
        filterChain.doFilter(request, response)
    }
}

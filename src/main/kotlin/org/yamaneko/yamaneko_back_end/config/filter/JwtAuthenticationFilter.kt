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

class JwtAuthenticationFilter: OncePerRequestFilter() {

    private val jwtUtil: JwtUtil = JwtUtil()

    @Throws ( ServletException::class )
    override fun doFilterInternal ( request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain ) {
        val authHeader = request.getHeader( "Authorization" )
        var username: String? = null
        var jwt: String? = null

        if ( authHeader != null && authHeader.startsWith("Bearer ") ) {
            jwt = authHeader.substring( 7 )
            username = jwtUtil.extractUsername( jwt )
        }

        if ( username != null && SecurityContextHolder.getContext().authentication == null  ) {
            if ( jwtUtil.validateToken( jwt.toString(), username ) ) {
                val authentication = UsernamePasswordAuthenticationToken ( username, null, emptyList() )
                authentication.details = WebAuthenticationDetailsSource().buildDetails( request )
                SecurityContextHolder.getContext().authentication = authentication
            }
        }
        filterChain.doFilter ( request, response )
    }

}
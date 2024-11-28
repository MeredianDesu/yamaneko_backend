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
    @Autowired private lateinit var jwtUtil: JwtUtil
    @Autowired private lateinit var customUserDetailsService: CustomUserDetailsService

    @Throws( ServletException::class )
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader( "Authorization" )
        var id: String? = null
        var jwt: String? = null

        if ( authHeader != null && authHeader.startsWith( "Bearer " ) ) {
            jwt = authHeader.substring( 7 )
            id = jwtUtil.extractUserId( jwt )
        }

        // Ensure JWT is not null and userID is extracted
        if ( id != null && jwt != null && SecurityContextHolder.getContext().authentication == null) {
            if ( jwtUtil.validateToken ( jwt, id ) ) {
                val userDetails = customUserDetailsService.loadUserByUsername( id )
                if( userDetails != null ) {
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails( request )
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        request.setAttribute( "AuthorizationToken", jwt )
        filterChain.doFilter( request, response )
    }
}

package org.yamaneko.yamaneko_back_end.service.user_details

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.yamaneko.yamaneko_back_end.entity.User

class CustomUserDetails( private val user: User ): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        val authorities = user.roles.map { role -> SimpleGrantedAuthority( role ) }

        return authorities
    }

    override fun getPassword(): String {

        return user.password
    }

    override fun getUsername(): String {

        return user.email
      /* return user.username */
    }

}
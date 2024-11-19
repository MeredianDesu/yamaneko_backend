package org.yamaneko.yamaneko_back_end.service.user_details

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.repository.UserRepository

@Service
class CustomUserDetailsService( private val userRepository: UserRepository ): UserDetailsService {
    // Receiving user ID
    override fun loadUserByUsername( userId: String? ): UserDetails? {

        return try {
            val user = userRepository.findById( userId?.toLong()!! )
            CustomUserDetails( user.get() )
        } catch ( e: UsernameNotFoundException ){
            print( "| $e | User not found with ID: $userId" )
            null
        }
    }
}
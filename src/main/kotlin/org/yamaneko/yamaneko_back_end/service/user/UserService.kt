package org.yamaneko.yamaneko_back_end.service.user

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest

interface UserService {
    fun getAllUsers(): List<UserDTO>
    fun registerUser( request: UserRegistrationRequest ): ResponseEntity<String>
}
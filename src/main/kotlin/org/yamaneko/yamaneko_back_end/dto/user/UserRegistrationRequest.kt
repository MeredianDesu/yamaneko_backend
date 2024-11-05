package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.Email

data class UserRegistrationRequest(
    val username: String,
    @field:Email( message = "Invalid email" ) val email: String,
    val password: String,
    val repeatedPassword: String
)

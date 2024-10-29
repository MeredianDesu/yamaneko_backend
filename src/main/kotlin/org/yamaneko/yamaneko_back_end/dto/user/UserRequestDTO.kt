package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserRequestDTO(
    @field:NotBlank( message = "Username cannot be null or blank" ) val username: String,
    @field:NotBlank(message = "Password cannot be null or blank" ) val password: String,
    @field:Email( message = "Email would be valid" ) val email: String,
    @field:NotNull( message = "Roles cannot be null" ) val roles: MutableSet<String>,
    val avatar: String
)

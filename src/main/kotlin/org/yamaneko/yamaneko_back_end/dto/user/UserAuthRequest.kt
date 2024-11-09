package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserAuthRequest(

    @field:Email( message = "Invalid email" )
    val email: String,

    @field:NotBlank( message = "Password required" )
    @Size( min = 6, message = "Minimum password length is 6")
    val password: String,
)

package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRegistrationRequest(

    @field:NotBlank( message = "Blank username" )
    val username: String,

    @field:Email( message = "Invalid email" )
    val email: String,

    @field:NotBlank( message = "Password is required" ) val password: String,
    @Size( min = 6, message = "Minimum password length is 6" )
    val repeatedPassword: String
)

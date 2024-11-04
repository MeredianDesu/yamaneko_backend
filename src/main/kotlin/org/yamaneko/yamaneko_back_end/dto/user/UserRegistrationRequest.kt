package org.yamaneko.yamaneko_back_end.dto.user

data class UserRegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val repeatedPassword: String
)

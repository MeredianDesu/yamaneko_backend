package org.yamaneko.yamaneko_back_end.dto.user

data class UserAuthRequest(
    val email: String,
    val password: String,
)

package org.yamaneko.yamaneko_back_end.dto.user

data class RegisteredUserResponse(
    val accessToken: String?, // JWT - token
    val refreshToken: String? // Refresh token
)

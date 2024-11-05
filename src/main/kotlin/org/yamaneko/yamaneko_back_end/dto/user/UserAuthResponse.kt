package org.yamaneko.yamaneko_back_end.dto.user

data class UserAuthResponse(
    val accessToken: String?, // JWT - token
    val refreshToken: String? // Refresh token
)

package org.yamaneko.yamaneko_back_end.customExceptions

class JwtExpirationException(message: String): RuntimeException(message) {}
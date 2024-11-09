package org.yamaneko.yamaneko_back_end.dto

data class ErrorResponse(
    val error: String,
    val message: Map<String, String?>
)

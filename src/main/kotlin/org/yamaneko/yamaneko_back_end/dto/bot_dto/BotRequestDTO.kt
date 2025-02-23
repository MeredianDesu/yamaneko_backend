package org.yamaneko.yamaneko_back_end.dto.bot_dto

data class BotRequestDTO(
    val id: Long? = null,
    val name: String? = "",
    val timestamp: String? = null,

    val user: String? = null,
    val address: String? = null,
    val method: String? =  null,
)

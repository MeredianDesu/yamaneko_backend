package org.yamaneko.yamaneko_back_end.dto.team

data class TeamRequestDTO(
    val name: String,
    val user: Long // user ID
)

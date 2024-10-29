package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.NotNull

data class UserTeamDTO(
    @field:NotNull( message = "ID cannot be null" ) val id: Long?,
    @field:NotNull( message = "Username cannot be null" ) val username: String?
)

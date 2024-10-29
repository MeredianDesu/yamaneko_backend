package org.yamaneko.yamaneko_back_end.dto.team

import jakarta.validation.constraints.NotNull
import org.yamaneko.yamaneko_back_end.dto.user.UserTeamDTO

data class TeamDTO(
    @field:NotNull( message = "ID cannot be null") val id: Long,
    @field:NotNull( message = "Name cannot be null" ) val name: String,
    @field:NotNull( message = "User cannot be null" ) val user: UserTeamDTO
)

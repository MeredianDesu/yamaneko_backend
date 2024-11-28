package org.yamaneko.yamaneko_back_end.dto.team

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class TeamRequestDTO(
    @field: NotBlank( message = "Name cannot be blank" ) val name: String,
    @field: NotNull( message = "User ID cannot be null" ) val user: Long // user ID
)

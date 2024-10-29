package org.yamaneko.yamaneko_back_end.dto.genre

import jakarta.validation.constraints.NotNull

data class GenreRequestDTO(
    @field:NotNull( message = "Name cannot be null" ) val name: String
)

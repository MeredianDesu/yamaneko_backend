package org.yamaneko.yamaneko_back_end.dto.genre

import jakarta.validation.constraints.NotNull


data class GenreDTO(
    @field:NotNull( message = "ID cannot be null" ) val id: Long,
    @field:NotNull( message = "Name cannot be null") val name: String
)

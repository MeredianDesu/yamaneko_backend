package org.yamaneko.yamaneko_back_end.dto.character

import jakarta.validation.constraints.NotNull

data class CharacterRequestDTO(
    @field:NotNull( message = "Original name cannot be null" ) val originalName: String,
    @field:NotNull( message = "Translated name cannot be null" ) val translatedName: String,
    val image: String?
)

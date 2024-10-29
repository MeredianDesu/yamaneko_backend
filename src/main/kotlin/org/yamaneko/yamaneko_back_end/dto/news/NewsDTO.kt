package org.yamaneko.yamaneko_back_end.dto.news

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class NewsDTO(
    @field:NotNull( message = "ID cannot be null" ) val id: Long,
    @field:NotNull( message = "Title cannot be null" ) val title: String,
    @field:NotNull( message = "Content cannot be null" ) val content: String,
    @field:NotNull( message = "Preview image cannot benull" ) val previewImg: String,
    @field:NotBlank( message = "Creation date cannot be null or blank" ) val createdAt: String
)

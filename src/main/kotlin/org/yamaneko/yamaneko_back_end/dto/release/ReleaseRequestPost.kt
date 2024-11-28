package org.yamaneko.yamaneko_back_end.dto.release

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yamaneko.yamaneko_back_end.dto.dubber.DubberRequestPost

data class ReleaseRequestPost(
    @field: NotBlank( message = "Original name cannot be blank" ) val originalName: String,
    @field: NotBlank( message = "Translated name cannot be blank" ) val translatedName: String,
    val posterImageUrl: String?,
    val previewVideoUrl: String?,
    val videoUrl: String?,
    @field: NotBlank( message = "Synopsis cannot be blank" ) val synopsis: String,
    @field: NotBlank( message = "Information cannot be blank" ) val info: String,
    @field: NotNull( message = "Genres cannot be null" ) val dubbers: List<DubberRequestPost>, // team`s ID
    @field: NotNull( message = "Genres cannot be blank" ) val genres: List<Long>, // genres ID
)

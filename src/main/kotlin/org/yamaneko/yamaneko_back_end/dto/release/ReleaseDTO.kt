package org.yamaneko.yamaneko_back_end.dto.release

import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO

data class ReleaseDTO(
    val id: Long,
    val originalName: String,
    val translatedName: String,
    val posterImageUrl: String?,
    val previewVideoUrl: String?,
    val videoUrl: String?,
    val sinopsis: String,
    val info: String,
    val dubbers: List<RolesDTO>,
    val genres: List<GenreDTO>,
    val uploadedAt: String
)

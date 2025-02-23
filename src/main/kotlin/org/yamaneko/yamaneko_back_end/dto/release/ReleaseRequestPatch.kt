package org.yamaneko.yamaneko_back_end.dto.release

import org.yamaneko.yamaneko_back_end.dto.dubber.DubberRequestPost

data class ReleaseRequestPatch(
    val originalName: String?,
    val translatedName: String? = null,
    val posterImageUrl: String? = null,
    val previewImageUrl: String? = null,
    val videoUrl: String? = null,
    val synopsis: String? = null,
    val info: String? = null,
    val dubbers: List<DubberRequestPost>? = null,
    val genres: List<Long>? = null,
)

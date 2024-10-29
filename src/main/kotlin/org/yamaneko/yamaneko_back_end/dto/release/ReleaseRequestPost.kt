package org.yamaneko.yamaneko_back_end.dto.release

import org.yamaneko.yamaneko_back_end.dto.dubber.DubberRequestPost

data class ReleaseRequestPost(
    val originalName: String,
    val translatedName: String,
    val posterImageUrl: String?,
    val previewVideoUrl: String?,
    val videoUrl: String?,
    val sinopsis: String,
    val info: String,
    val dubbers: List<DubberRequestPost>, // team`s ID
    val genres: List<Long>, // genres ID
)

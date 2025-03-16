package org.yamaneko.yamaneko_back_end.dto.release

import org.yamaneko.yamaneko_back_end.dto.dubber.DubberRequestPost
import org.yamaneko.yamaneko_back_end.dto.episode.EpisodeDTO

data class ReleaseRequestPatch(
    val originalName: String?,
    val translatedName: String? = null,
    val ageRestriction: String? = null,
    val maxEpisodes: Int? = null,
    val status: String? = null,
    val posterImageUrl: String? = null,
    val previewVideoUrl: String? = null,
    val episodes: List<EpisodeDTO>? = null,
    val videoUrl: String? = null,
    val synopsis: String? = null,
    val info: String? = null,
    val dubbers: List<DubberRequestPost>? = null,
    val genres: List<Long>? = null,
)

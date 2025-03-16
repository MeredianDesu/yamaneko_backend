package org.yamaneko.yamaneko_back_end.dto.episode

data class EpisodeDTO(
    val episodeNumber: Long,
    val episodeName: String = "",
    val qualities: MutableList<EpisodeQualityDTO>,
)

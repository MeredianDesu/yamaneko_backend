package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.episode.EpisodeQualityDTO
import org.yamaneko.yamaneko_back_end.entity.EpisodeQuality

class EpisodeQualitiesMapper {
    fun toDTO( episodeQuality: EpisodeQuality ): EpisodeQualityDTO{
        return EpisodeQualityDTO(
            quality = episodeQuality.quality,
            url = episodeQuality.videoUrl,
        )
    }
}
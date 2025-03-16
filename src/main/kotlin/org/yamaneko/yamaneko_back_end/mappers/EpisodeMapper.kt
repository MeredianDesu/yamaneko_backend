package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.episode.EpisodeDTO
import org.yamaneko.yamaneko_back_end.entity.Episode
import org.yamaneko.yamaneko_back_end.entity.EpisodeQuality
import org.yamaneko.yamaneko_back_end.entity.Release

class EpisodeMapper{
    private val episodeQualitiesMapper: EpisodeQualitiesMapper = EpisodeQualitiesMapper()

    fun toDTO( episode: Episode ): EpisodeDTO {
        return EpisodeDTO(
            episodeNumber = episode.episodeNumber,
            episodeName = episode.episodeName,
            qualities = episode.qualities.map { episodeQualitiesMapper.toDTO( it ) }.toMutableList(),
        )
    }

    fun toEntity( episodeDTO: EpisodeDTO, release: Release, episodeNum: Long ): Episode {
        val episode = Episode(release = release).apply {
            episodeNumber = episodeNum
            episodeName = episodeDTO.episodeName
            qualities = episodeDTO.qualities.map { qualityDTO ->
                EpisodeQuality(episode = this).apply {
                    quality = qualityDTO.quality
                    videoUrl = qualityDTO.url
                }
            }.toMutableList()
        }
        return episode
    }
}
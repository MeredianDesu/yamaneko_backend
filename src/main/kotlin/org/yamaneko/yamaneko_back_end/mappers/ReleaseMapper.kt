package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.entity.Release

class ReleaseMapper{

    private val episodeMapper: EpisodeMapper = EpisodeMapper()

    fun toDTO( release: Release ): ReleaseDTO {

        return ReleaseDTO(
            id = release.id,
            originalName = release.originalName,
            translatedName = release.translatedName,
            ageRestriction = release.ageRestriction,
            maxEpisodes = release.maxEpisodes,
            status = release.status,
            posterImageUrl = release.posterImageUrl,
            previewVideoUrl = release.previewVideoUrl,
            episodes = release.episodes.map { episodeMapper.toDTO( it ) }.toMutableList(),
            synopsis = release.synopsis,
            info = release.info,
            dubbers = release.dubbers.map{ dubber ->
                RolesDTO(
                    characterId = dubber.characterId,
                    characterOriginalName = dubber.characterOriginalName,
                    characterTranslatedName = dubber.characterTranslatedName,
                    characterImage = dubber.characterImage,
                    dubberName = dubber.dubberName,
                )
            },
            genres = release.genres.map { genre ->
                GenreDTO(
                    id = genre.id,
                    name = genre.name,
                )
            },
            updatedAt = release.updatedAt,
            uploadedAt = release.uploadedAt,
        )
    }
}
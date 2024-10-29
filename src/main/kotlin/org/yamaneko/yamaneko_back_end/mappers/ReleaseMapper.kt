package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.entity.Release

class ReleaseMapper{

    fun toDTO( release: Release ): ReleaseDTO {

        return ReleaseDTO(
            id = release.id,
            originalName = release.originalName,
            translatedName = release.translatedName,
            posterImageUrl = release.posterImageUrl,
            previewVideoUrl = release.previewVideoUrl,
            videoUrl = release.videoUrl,
            sinopsis = release.sinopsis,
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
            uploadedAt = release.uploadedAt
        )
    }

}
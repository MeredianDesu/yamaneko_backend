package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.dubber.DubberDTO
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
            dubbers = release.dubbers.map{ i ->
                DubberDTO(
                    dubberId = i.dubberId,
                    dubberName = i.dubberName,
                    characters = i.characters.map { character ->
                        CharacterDTO(
                            id = character.id,
                            originalName = character.originalName,
                            translatedName = character.translatedName,
                            image = character.image,
                        )
                    }
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
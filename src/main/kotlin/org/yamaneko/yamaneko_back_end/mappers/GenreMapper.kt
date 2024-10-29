package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO
import org.yamaneko.yamaneko_back_end.entity.Genre

class GenreMapper {

    fun toDTO( genre: Genre ): GenreDTO {

        return GenreDTO(
            id = genre.id,
            name = genre.name
        )
    }

    fun toEntity( genreDTO: GenreDTO ): Genre {
        val genre = Genre()
        genre.name = genreDTO.name

        return genre
    }
}
package org.yamaneko.yamaneko_back_end.service.genre

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO

interface
GenreService {
    fun getAllGenres(): List<GenreDTO>
    fun getGenreById( genreId: Long ): GenreDTO?
    fun removeGenre( genreId: Long ): ResponseEntity<Any>
}
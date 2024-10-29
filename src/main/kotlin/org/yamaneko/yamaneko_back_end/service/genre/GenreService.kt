package org.yamaneko.yamaneko_back_end.service.genre

import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO

interface GenreService {
    fun getAllGenres(): List<GenreDTO>
}
package org.yamaneko.yamaneko_back_end.service.genre

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO
import org.yamaneko.yamaneko_back_end.mappers.GenreMapper
import org.yamaneko.yamaneko_back_end.repository.GenreRepository

@Service
class GenreServiceImp: GenreService {

    private val genreMapper = GenreMapper()

    @Autowired
    private lateinit var genreRepository: GenreRepository

    override fun getAllGenres(): List< GenreDTO > {

        return genreRepository.findAll().map{ genreMapper.toDTO( it ) }
    }
}
package org.yamaneko.yamaneko_back_end.service.genre

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
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

    override fun getGenreById( genreId: Long ): GenreDTO {
        val genre = genreRepository.findById(genreId).get()

        return genreMapper.toDTO(genre)
    }

    override fun removeGenre( genreId: Long ): ResponseEntity<Any> {

        return if( genreRepository.existsById( genreId ) ){
            genreRepository.deleteById( genreId )
            ResponseEntity.ok().build()
        }
        else
            ResponseEntity.notFound().build()
    }
}
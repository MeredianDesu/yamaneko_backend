package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.genre.GenreDTO
import org.yamaneko.yamaneko_back_end.dto.genre.GenreRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Genre
import org.yamaneko.yamaneko_back_end.repository.GenreRepository
import org.yamaneko.yamaneko_back_end.service.genre.GenreService

@RestController
@RequestMapping("/api/v1/genres")
class GenreController(
    @Autowired private val genreService: GenreService,
    @Autowired private val genreRepository: GenreRepository
) {

    @Operation( summary = "Get all genres form DB.")
    @GetMapping("")
    fun getGenres(): ResponseEntity< List< GenreDTO > > {
        val genresList = genreService.getAllGenres()

        return if( genresList.isEmpty() )
            ResponseEntity.status( HttpStatus.NO_CONTENT ).build()
        else
            ResponseEntity.status( HttpStatus.OK ).body( genresList )
    }

    @Operation( summary = "Create a new genre." )
    @PostMapping("")
    fun createGenre( @Valid @RequestBody request: GenreRequestDTO): ResponseEntity<GenreDTO>{
        val genre = Genre()
        genre.name = request.name

        val savedGenre = genreRepository.save( genre )
        val response = GenreDTO(
            id = savedGenre.id,
            name = savedGenre.name
        )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }
}
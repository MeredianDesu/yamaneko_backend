package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO
import org.yamaneko.yamaneko_back_end.service.character.CharacterService

@RestController
@RequestMapping( "/api/v1/characters")
class CharacterController(
    @Autowired val characterService: CharacterService
) {

    @Operation( summary = "Get all characters form DB." )
    @GetMapping("")
    fun getCharacters(): ResponseEntity< List< CharacterDTO > > {
        val characterList = characterService.getAllCharacters()

        return if( characterList.isEmpty() )
            ResponseEntity.status( HttpStatus.NO_CONTENT ).build()
        else
            ResponseEntity.ok( characterList )
    }

    @Operation( summary = "Create character." )
    @PostMapping("")
    fun saveCharacter( @RequestBody request: CharacterRequestDTO ): ResponseEntity<CharacterDTO>{
        val response = characterService.createCharacter( request )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }
}
package org.yamaneko.yamaneko_back_end.service.character

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO

interface CharacterService {
    fun getAllCharacters(): List<CharacterDTO>
    fun createCharacter( request: CharacterRequestDTO ): CharacterDTO
    fun removeCharacter( characterId: Long ): ResponseEntity<Any>
    fun getCharacterById( id: Long ): CharacterDTO?
}
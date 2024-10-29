package org.yamaneko.yamaneko_back_end.service.character

import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO

interface CharacterService {
    fun getAllCharacters(): List<CharacterDTO>
    fun createCharacter( request: CharacterRequestDTO ): CharacterDTO
}
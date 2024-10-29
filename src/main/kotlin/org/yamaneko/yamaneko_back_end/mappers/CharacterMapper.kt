package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.entity.Character

class CharacterMapper {

    fun toDTO( character: Character ): CharacterDTO {

        return CharacterDTO(
            id = character.id,
            originalName = character.originalName,
            translatedName = character.translatedName,
            image = character.image
        )
    }

    fun toEntity( characterDTO: CharacterDTO ): Character{
        val character = Character()
        character.originalName = characterDTO.originalName
        character.translatedName = characterDTO.translatedName
        character.image = characterDTO.image

        return character
    }
}
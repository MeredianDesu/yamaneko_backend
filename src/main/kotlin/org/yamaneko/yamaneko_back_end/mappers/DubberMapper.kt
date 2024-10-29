package org.yamaneko.yamaneko_back_end.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.dubber.DubberDTO
import org.yamaneko.yamaneko_back_end.entity.Character
import org.yamaneko.yamaneko_back_end.repository.TeamRepository

class DubberMapper( @Autowired private val teamRepository: TeamRepository ) {

    fun toDTO( teamId: Long, character: List<Character> ): DubberDTO {

        return DubberDTO(
            dubberId = teamId,
            dubberName = teamRepository.findById( teamId ).get().name,
            characters = character.map{
                CharacterDTO(
                    id = it.id,
                    originalName = it.originalName,
                    translatedName = it.translatedName,
                    image = it.image,
                )
            }
        )
    }
}
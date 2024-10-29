package org.yamaneko.yamaneko_back_end.service.character

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Character
import org.yamaneko.yamaneko_back_end.mappers.CharacterMapper
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository

@Service
class CharacterServiceImpl: CharacterService {

    private val characterMapper = CharacterMapper()

    @Value("\${yamaneko.server}")
    private lateinit var filesServerUrl: String

    @Autowired
    private lateinit var characterRepository: CharacterRepository

    override fun getAllCharacters(): List<CharacterDTO> {

        return characterRepository.findAll().map{ characterMapper.toDTO( it ) }
    }

    override fun createCharacter( request: CharacterRequestDTO ): CharacterDTO {
        val character = Character()
        character.originalName = request.originalName
        character.translatedName = request.translatedName
        character.image = request.image ?: "${filesServerUrl}mascot.jfif"

        val savedCharacter = characterRepository.save( character )

        return characterMapper.toDTO( savedCharacter )
    }
}
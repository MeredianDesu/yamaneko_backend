package org.yamaneko.yamaneko_back_end.service.character

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestPatch
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

    override fun removeCharacter(characterId: Long): ResponseEntity<Any> {
        val character = characterRepository.findById( characterId )

        return if( !character.isEmpty ) {
            characterRepository.deleteById( characterId )
            ResponseEntity.status(HttpStatus.OK).build()
        }
        else
            ResponseEntity.status( HttpStatus.NOT_FOUND ).build()
    }

    override fun getCharacterById( id: Long ): CharacterDTO? {
        val character = characterRepository.findById( id ).get()

        return characterMapper.toDTO( character )
    }

    override fun updateCharacter( request: CharacterRequestPatch, characterId: Long ): CharacterDTO {
        val character = characterRepository.findById( characterId ).orElseThrow { NoSuchElementException( "Character with id $characterId does not exist" ) }

        request.originalName?.let { character.originalName = it }
        request.translatedName?.let { character.translatedName = it }
        request.image?.let { character.image = it }

        characterRepository.save( character )

        return characterMapper.toDTO( character )
    }
}
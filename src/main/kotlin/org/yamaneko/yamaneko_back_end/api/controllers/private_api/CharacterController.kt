package org.yamaneko.yamaneko_back_end.api.controllers.private_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.ErrorResponse
import org.yamaneko.yamaneko_back_end.dto.bot_dto.BotRequestDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestDTO
import org.yamaneko.yamaneko_back_end.dto.character.CharacterRequestPatch
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository
import org.yamaneko.yamaneko_back_end.service.character.CharacterService
import org.yamaneko.yamaneko_back_end.service.discord_bot.BotService
import org.yamaneko.yamaneko_back_end.utils.AuthenticatedUserData
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping( "/api/characters/v1")
class CharacterController(
    @Autowired val characterService: CharacterService,
    private val characterRepository: CharacterRepository,
    private val botService: BotService
) {

    private val userData = AuthenticatedUserData()

    @Operation( summary = "Get all characters form DB." )
    @GetMapping("")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = CharacterDTO::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Unprocessable entity",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
        ]
    )
    fun getCharacters(): ResponseEntity< List< CharacterDTO > > {
        val characterList = characterService.getAllCharacters()

        return if( characterList.isEmpty() )
            ResponseEntity.status( HttpStatus.NO_CONTENT ).build()
        else
            ResponseEntity.ok( characterList )
    }

    @Operation( summary = "Get character by ID.")
    @GetMapping("{characterId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = CharacterDTO::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Unprocessable entity",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
        ]
    )
    fun getCharacterById( @Parameter( description = "Enter id of character", example = "1" ) @PathVariable( required = false ) characterId: Long ): ResponseEntity<CharacterDTO>{
        val character = characterService.getCharacterById( characterId )

        return ResponseEntity.status( HttpStatus.OK ).body( character )
    }


    @Operation( summary = "Create character." )
    @PostMapping("")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = CharacterDTO::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Unprocessable entity",
                content = [ Content( mediaType = "text/plain", schema = Schema( implementation = ErrorResponse::class ) ) ]
            ),
        ]
    )
    fun saveCharacter( @RequestBody request: CharacterRequestDTO, httpRequest: HttpServletRequest ): ResponseEntity<CharacterDTO>{
        val response = characterService.createCharacter( request )

        val botNotificationBody = BotRequestDTO(
            id = response.id,
            name = response.originalName,
            timestamp = DateFormatter().dateToString(Date()),

            user = userData.getAuthenticatedUser(),
            address = httpRequest.remoteAddr,
            method = httpRequest.method,
        )
        botService.createCharacterNotification( botNotificationBody )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }

    @Operation( summary = "Remove character." )
    @DeleteMapping("{characterId}")
    fun removeCharacter( @Parameter( description = "Enter id of character", example = "1" ) @PathVariable( required = false ) characterId: Long ): ResponseEntity<Any>{
        val status = characterService.removeCharacter( characterId ).statusCode

        println( status )

        return when( status ){
            HttpStatus.OK -> ResponseEntity.status( HttpStatus.OK ).body( "Character removed successfully" )
            HttpStatus.NOT_FOUND -> ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Not found" )
            else -> ResponseEntity.unprocessableEntity().build()
        }
    }

    @Operation( summary = "Update character." )
    @PatchMapping("{characterId}")
    fun patchCharacter( @PathVariable characterId: Long, @RequestBody request: CharacterRequestPatch, httpRequest: HttpServletRequest ): ResponseEntity<Any>{
        val characterName = characterRepository.findById( characterId ).get().translatedName

        characterService.updateCharacter( request, characterId )
        val botNotificationBody = BotRequestDTO(
            id = characterId,
            name = characterName,
            timestamp = DateFormatter().dateToString(Date()),

            user = userData.getAuthenticatedUser(),
            address = httpRequest.remoteAddr,
            method = httpRequest.method,
        )
        botService.createCharacterNotification( botNotificationBody )

        return ResponseEntity.ok().build()
    }
}
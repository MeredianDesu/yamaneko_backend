package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthResponse
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.repository.UserRefreshTokensRepository
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.repository.UserTokensRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService

@RestController
@RequestMapping("/api/auth/v1/")
class AuthController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
    private val userTokensRepository: UserTokensRepository,
    private val userRefreshTokensRepository: UserRefreshTokensRepository
){
    @Operation( summary = "Auth user." )
    @PostMapping( "/login" )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Authorized",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = UserAuthResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Unauthorized" ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "User not found" ) ) ]
            )
        ]
    )
    fun authUser( @Schema( type = "" ) @Valid @RequestBody request: UserAuthRequest ): ResponseEntity<Any>{
        val status = userService.authUser( request )

        if( status.statusCode == HttpStatus.NOT_FOUND )
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( status.body )
        if( status.statusCode == HttpStatus.UNAUTHORIZED )
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( status.body )

        val tokens = status.body?.split( '|' )

        val response = UserAuthResponse(
            accessToken = tokens?.get( 0 ),
        )

        return ResponseEntity.status( HttpStatus.OK ).body( response )
    }

    @Operation( summary = "Create a new user." )
    @PostMapping("/register")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "User created",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = UserAuthResponse::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
            ApiResponse(
                responseCode = "409",
                description = "User already exists",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "User already exists" ) ) ]
            ),
        ]
    )
    fun createUser( @Valid @RequestBody request: UserRegistrationRequest ): ResponseEntity<Any>{
        val status = userService.registerUser( request )

        if( status.statusCode == HttpStatus.CONFLICT )
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "A user with this email address is already registered" )
        if( status.statusCode == HttpStatus.UNPROCESSABLE_ENTITY )
            return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( "Password doesn't match" )

        val tokens = status.body?.split('|')

        val response = UserAuthResponse(
            accessToken = tokens?.get(0)
        )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }

    @Operation( summary = "Refresh access token" )
    @PostMapping( "/refresh" )
    fun refreshToken( @RequestAttribute( "AuthorizationToken" ) @Parameter( description = "JWT token." ) authHeader: String ): ResponseEntity<Any>{
        var accessToken = authHeader.removePrefix("Bearer ").trim()
        val userIdByAccessToken = userTokensRepository.findByToken( accessToken )?.user?.id ?: return ResponseEntity.status( HttpStatus.NOT_FOUND ).build()
        if( userRefreshTokensRepository.findByUser( userIdByAccessToken )?.token != null ){
            val user = userRepository.findById( userIdByAccessToken ).get()
            accessToken = userService.generateAndSaveAccessToken( user )
        }
        else{
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).build()
        }

        return ResponseEntity.status( HttpStatus.OK ).body( accessToken )
    }
}
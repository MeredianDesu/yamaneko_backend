package org.yamaneko.yamaneko_back_end.api.controllers.private_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService
import org.yamaneko.yamaneko_back_end.utils.JwtUtil

@RestController
@RequestMapping( "/api/users/v1" )
@Validated
class UserController(
    @Autowired
    private var userService: UserService,
    private val userRepository: UserRepository,
) {

    private val userMapper = UserMapper()

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Operation( summary = "Get all users." )
    @GetMapping("")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = UserDTO::class ) ) ],
            ),
            ApiResponse(
                responseCode = "204",
                description = "No content",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "User not found" ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
        ]
    )
    fun getUsers(): List<UserDTO> {

        return userService.getAllUsers()
    }

    @Operation( summary = "Get user by JWT." )
    @GetMapping( "/user" )
    fun getUserByJWT(
        @RequestAttribute( "AuthorizationToken" ) @Parameter( description = "accessToken") authHeader: String,
    ): ResponseEntity<UserDTO> {
        val accessToken = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtUtil.extractUserId( accessToken ) ?: return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).build()
        val user = userRepository.findById( userId.toLong() ).orElse( null ) ?: return ResponseEntity.status( HttpStatus.NOT_FOUND ).build()

        return ResponseEntity.status( HttpStatus.OK ).body( userMapper.toDTO( user ) )
    }
}
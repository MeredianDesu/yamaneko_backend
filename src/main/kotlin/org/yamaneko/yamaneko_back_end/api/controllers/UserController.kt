package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthResponse
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService

@RestController
@RequestMapping( "/api/v1/users" )
class UserController(
    @Autowired
    private var userService: UserService,
    private val userRepository: UserRepository,
) {

    private val userMapper = UserMapper()

    @Operation( summary = "Get all users." )
    @GetMapping("")
    @ApiResponses(
        value = [
            ApiResponse( responseCode = "200", description = "OK"),
            ApiResponse( responseCode = "204", description = "No content"),
            ApiResponse( responseCode = "400", description = "Bad request"),
        ]
    )
    fun getUsers(): List<UserDTO> {

        return userService.getAllUsers()
    }

    @Operation( summary = "Create a new user." )
    @PostMapping("/register")
    @ApiResponses(
        value = [
            ApiResponse( responseCode = "201", description = "User created"),
            ApiResponse( responseCode = "400", description = "Bad request"),
            ApiResponse( responseCode = "409", description = "User already exists"),
            ApiResponse( responseCode = "422", description = "Password mismatch"),
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
            accessToken = tokens?.get(0),
            refreshToken = tokens?.get(1)
        )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }

    @Operation( summary = "Auth user." )
    @PostMapping( "/login" )
//    @ApiResponses()
    fun authUser( @RequestBody request: UserAuthRequest ): ResponseEntity<Any>{
        val status = userService.authUser( request )

        if( status.statusCode == HttpStatus.NOT_FOUND )
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found" )
        if( status.statusCode == HttpStatus.UNAUTHORIZED )
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Invalid credentials" )

        val tokens = status.body?.split('|')

        val response = UserAuthResponse(
            accessToken = tokens?.get(0),
            refreshToken = tokens?.get(1)
        )

        return ResponseEntity.status( HttpStatus.OK ).body( response )
    }
}
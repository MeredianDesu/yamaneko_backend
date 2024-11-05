package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    @PostMapping("")
    @ApiResponses(
        value = [
            ApiResponse( responseCode = "201", description = "User created"),
            ApiResponse( responseCode = "400", description = "Bad request"),
            ApiResponse( responseCode = "409", description = "User already exists"),
            ApiResponse( responseCode = "422", description = "Password mismatch"),
        ]
    )
    fun createUser( @RequestBody request: UserRegistrationRequest ): ResponseEntity<String>{
        val response = userService.registerUser( request )

        return response
    }
}
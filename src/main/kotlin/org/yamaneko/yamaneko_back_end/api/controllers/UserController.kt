package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
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
    fun getUsers(): List<UserDTO> {

        return userService.getAllUsers()
    }

    @Operation( summary = "Create a new user." )
    @PostMapping("")
    fun createUser( @RequestBody request: UserRegistrationRequest ): ResponseEntity<String>{
//        val response = UserDTO(
//            id = savedUser.id,
//            username = request.username,
//            password = savedUser.password,
//            email = request.email,
//            roles = savedUser.roles,
//            avatar = savedUser.avatar,
//            createdAt = savedUser.createdAt,
//        )

        val response = userService.registerUser( request )

        return response

    }
}
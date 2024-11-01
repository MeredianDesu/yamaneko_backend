package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRequestDTO
import org.yamaneko.yamaneko_back_end.entity.User
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    fun createUser( @Valid @RequestBody request: UserRequestDTO): ResponseEntity<UserDTO>{
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = currentDate.format( formatter )

        val user = User()
        user.username = request.username
        user.password = request.password
        user.email = request.email
        user.roles = request.roles.toMutableSet()
        user.avatar = request.avatar
        user.createdAt = date

        val savedUser = userRepository.save( user )
        val response = UserDTO(
            id = savedUser.id,
            username = request.username,
            password = savedUser.password,
            email = request.email,
            roles = savedUser.roles,
            avatar = savedUser.avatar,
            createdAt = savedUser.createdAt,
        )

        return ResponseEntity.ok( response )

    }
}
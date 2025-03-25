package org.yamaneko.yamaneko_back_end.api.controllers.private_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService
import org.yamaneko.yamaneko_back_end.utils.JwtUtil

@Tag(name = "{ v1 } Users API")
@RestController
@RequestMapping("/api/users/v1")
@Validated
class UserController(
  @Autowired private var userService: UserService,
  private val userRepository: UserRepository,
) {
  
  private val userMapper = UserMapper()
  
  @Autowired
  private lateinit var jwtUtil: JwtUtil
  
  @Operation(summary = "Get all users.")
  @GetMapping("")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserDTO::class))],
      ),
      ApiResponse(
        responseCode = "204",
        description = "No content",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "User not found"))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Bad request"))]
      ),
    ]
  )
  fun getUsers(): ResponseEntity<List<UserDTO>> {
    val users = userService.getAllUsers()
    
    return if(users.isEmpty()) {
      ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    } else {
      ResponseEntity.status(HttpStatus.OK).body(users)
    }
  }
  
  @Operation(summary = "Get user by JWT.")
  @GetMapping("/user")
  fun getUserByJWT(
    @RequestHeader("Authorization", required = false) authHeader: String?): ResponseEntity<UserDTO> {
    if(authHeader.isNullOrBlank() || ! authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
    
    val accessToken = authHeader.removePrefix("Bearer ").trim()
    val userId =
      jwtUtil.extractUserId(accessToken)?.toLongOrNull() ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .build()
    
    val user =
      userRepository.findById(userId).orElse(null) ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    
    return ResponseEntity.ok(userMapper.toDTO(user))
  }
  
}
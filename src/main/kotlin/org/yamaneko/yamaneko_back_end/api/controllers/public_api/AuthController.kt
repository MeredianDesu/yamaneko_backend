package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthResponse
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.repository.UserRefreshTokensRepository
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.repository.UserTokensRepository
import org.yamaneko.yamaneko_back_end.service.user.UserService

@Tag(name = "{ v1 } Auth API")
@RestController
@RequestMapping("/api/auth/v1/")
class AuthController(
  @Autowired private val userService: UserService,
  @Autowired private val userRepository: UserRepository,
  private val userTokensRepository: UserTokensRepository,
  private val userRefreshTokensRepository: UserRefreshTokensRepository
) {
  
  @Operation(summary = "Auth user.")
  @PostMapping("/login")
  fun authUser(@Schema(type = "") @Valid @RequestBody request: UserAuthRequest): ResponseEntity<Any> {
    val status = userService.authUser(request)
    
    when(status.statusCode) {
      HttpStatus.NOT_FOUND -> return ResponseEntity.notFound().build()
      HttpStatus.UNAUTHORIZED -> return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
      else -> {
        val tokens = status.body?.split('|')
        
        val response = UserAuthResponse(
          accessToken = tokens?.get(0),
        )
        
        return ResponseEntity.status(HttpStatus.OK).body(response)
      }
    }
  }
  
  @Operation(summary = "Create a new user.")
  @PostMapping("/register")
  fun createUser(@Valid @RequestBody request: UserRegistrationRequest): ResponseEntity<Any> {
    val status = userService.registerUser(request)
    
    if(status.statusCode == HttpStatus.CONFLICT) return ResponseEntity.status(HttpStatus.CONFLICT)
      .body("A user with this email address is already registered")
    if(status.statusCode == HttpStatus.UNPROCESSABLE_ENTITY) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body("Password doesn't match")
    
    val tokens = status.body?.split('|')
    
    val response = UserAuthResponse(
      accessToken = tokens?.get(0)
    )
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response)
  }
  
  @Operation(summary = "Refresh access token")
  @PostMapping("/refresh")
  fun refreshToken(@RequestAttribute("AuthorizationToken") @Parameter(description = "JWT token.") authHeader: String): ResponseEntity<Any> {
    var newAccessToken = authHeader.removePrefix("Bearer ").trim()
    val userIdByAccessToken =
      userTokensRepository.findByToken(newAccessToken)?.user?.id ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .build()
    
    val refreshToken = userRefreshTokensRepository.findByUser(userIdByAccessToken)?.token
    if(refreshToken != null) {
      val user = userRepository.findById(userIdByAccessToken).get()
      newAccessToken = userService.generateAndSaveAccessToken(user)
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
    
    val response = UserAuthResponse(
      accessToken = newAccessToken,
    )
    
    return ResponseEntity.status(HttpStatus.OK).body(response)
  }
  
}
package org.yamaneko.yamaneko_back_end.service.user

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.ErrorResponse
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserPatchRequestDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.entity.*
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.AchievementRepository
import org.yamaneko.yamaneko_back_end.repository.UserRefreshTokensRepository
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.repository.UserTokensRepository
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import org.yamaneko.yamaneko_back_end.utils.JwtUtil
import org.yamaneko.yamaneko_back_end.utils.PasswordSecurity
import java.util.*

@Service
class UserServiceImpl: UserService {
  
  private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
  
  @Autowired
  private lateinit var achievementRepository: AchievementRepository
  
  @Autowired
  lateinit var userRepository: UserRepository
  
  @Autowired
  lateinit var userTokensRepository: UserTokensRepository
  
  @Autowired
  lateinit var refreshTokensRepository: UserRefreshTokensRepository
  
  @Autowired
  lateinit var jwtUtil: JwtUtil
  
  @Autowired
  lateinit var dateFormatter: DateFormatter
  
  @Autowired
  lateinit var passwordSecurity: PasswordSecurity
  
  private var userMapper = UserMapper()
  
  override fun getAllUsers(): List<UserDTO> {
    val userList = userRepository.findAll()
    return userList.map { userMapper.toDTO(it) }
  }
  
  override fun registerUser(request: UserRegistrationRequest): ResponseEntity<String> {
    val response = validateUserRequest(request)
    if(response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body("Password doesn't match")
    if(response.statusCode == HttpStatus.CONFLICT) return ResponseEntity.status(HttpStatus.CONFLICT)
      .body("A user with this email address is already registered")
    
    val date = dateFormatter.dateToString(Date())
    
    val user = User().apply {
      username = request.username
      password = passwordSecurity.hashPassword(request.password)
      email = request.email
      createdAt = date
    }
    
    userRepository.save(user)
    
    addAchievementToUser(user.id, 1)
    
    val token = jwtUtil.generateToken(user)
    val refreshToken = jwtUtil.generateRefreshToken()
    
    val userToken = UserToken(
      user = user,
      jwtToken = token,
      createdAt = dateFormatter.dateToString(jwtUtil.extractCreation(token) !!),
      expiresAt = dateFormatter.dateToString(jwtUtil.extractExpiration(token) !!)
    )
    val userRefreshToken = RefreshToken(
      user = user,
      token = refreshToken["token"] !!,
      createdAt = refreshToken["issuedAt"] !!,
      expiresAt = refreshToken["expirationDate"] !!
    )
    
    userTokensRepository.save(userToken)
    refreshTokensRepository.save(userRefreshToken)
    
    return ResponseEntity.status(HttpStatus.CREATED)
      .body("$token|${refreshToken["token"] !!}")
  }
  
  @Transactional
  override fun authUser(request: UserAuthRequest): ResponseEntity<String> {
    val user =
      userRepository.findByEmail(request.email) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("User not found")
    
    if(! passwordSecurity.verifyPassword(request.password, user.password)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Invalid credentials")
    }
    
    val refreshToken = refreshTokensRepository.findByUser(user.id)?.token
    return if(refreshToken != null) {
      val newAccessToken = generateAndSaveAccessToken(user)
      ResponseEntity.status(HttpStatus.OK)
        .body("$newAccessToken|$refreshToken")
    } else {
      val newAccessToken = generateAndSaveAccessToken(user)
      val newRefreshToken = generateAndSaveRefreshToken(user)
      ResponseEntity.status(HttpStatus.OK)
        .body("$newAccessToken|$newRefreshToken")
    }
  }
  
  override fun generateAndSaveAccessToken(user: User): String {
    val newAccessToken = jwtUtil.generateToken(user)
    val userToken = UserToken(
      user = user,
      jwtToken = newAccessToken,
      createdAt = dateFormatter.dateToString(jwtUtil.extractCreation(newAccessToken) !!),
      expiresAt = dateFormatter.dateToString(jwtUtil.extractExpiration(newAccessToken) !!)
    )
    userTokensRepository.save(userToken)
    return newAccessToken
  }
  
  private fun generateAndSaveRefreshToken(user: User): String {
    val newRefreshToken = jwtUtil.generateRefreshToken()
    val refreshTokenInstance = RefreshToken(
      user = user,
      token = newRefreshToken["token"] !!,
      createdAt = newRefreshToken["issuedAt"] !!,
      expiresAt = newRefreshToken["expirationDate"] !!
    )
    refreshTokensRepository.save(refreshTokenInstance)
    return newRefreshToken["token"] !!
  }
  
  private fun validateUserRequest(request: UserRegistrationRequest): ResponseEntity<String> {
    val userWithEmail = userRepository.findByEmail(request.email)
    
    if(request.password != request.repeatedPassword) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body("Password doesn't match")
    if(userWithEmail != null) return ResponseEntity.status(HttpStatus.CONFLICT)
      .body("A user with this email address is already registered")
    
    return ResponseEntity.status(HttpStatus.OK)
      .body("Check passed successfully")
  }
  
  override fun addAchievementToUser(userId: Long, achievementId: Long): ResponseEntity<Any> {
    val user =
      userRepository.findById(userId)
        .orElseThrow { RuntimeException("User not found") }
    val achievement =
      achievementRepository.findById(achievementId)
        .orElseThrow { RuntimeException("Achievement not found") }
    
    val userAchievement = user.userAchievements.find { it.achievement?.id == achievementId }
    
    return if(userAchievement == null) {
      val newUserAchievement = UserAchievement().apply {
        this.user = user
        this.achievement = achievement
        this.receivedAt = dateFormatter.dateToString(Date())
      }
      
      user.userAchievements.add(newUserAchievement)
      
      userRepository.save(user)
      
      ResponseEntity.status(HttpStatus.CREATED)
        .build()
    } else {
      val err = ErrorResponse(error = "Conflict", message = mapOf("details" to "Achievement already set"))
      ResponseEntity.status(HttpStatus.CONFLICT)
        .body(err)
    }
  }
  
  override fun updateUserData(request: UserPatchRequestDTO, username: String): Boolean {
    val user = userRepository.findByUsername(username) ?: return false
    
    logger.info("Updating user data: ${user.username}")
    
    // Обновляем аватар только если поле пришло не-null и не пустое
    request.avatar?.takeIf { it.isNotBlank() }
      ?.let {
        user.avatar = it
        logger.info("Avatar updated to '$it'")
      }
    
    // Обновляем header только если поле пришло не-null и не пустое
    request.header?.takeIf { it.isNotBlank() }
      ?.let {
        user.header = it
        logger.info("Header updated to '$it'")
      }
    
    userRepository.save(user)
    val newUser = userRepository.findByUsername(username)
    logger.info(newUser.toString())
    
    return true
  }
  
  override fun addToFavorite(user: User, release: Release): Boolean {
    if(user.favoriteReleases.contains(release)) {
      
      return false
    }
    
    user.favoriteReleases.add(release)
    release.likedByUsers.add(user)
    userRepository.save(user)
    
    return true
  }
  
}

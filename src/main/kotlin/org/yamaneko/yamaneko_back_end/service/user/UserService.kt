package org.yamaneko.yamaneko_back_end.service.user

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserPatchRequestDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.entity.Release
import org.yamaneko.yamaneko_back_end.entity.User

interface UserService {
  
  fun getAllUsers(): List<UserDTO>
  fun registerUser(request: UserRegistrationRequest): ResponseEntity<String>
  fun authUser(request: UserAuthRequest): ResponseEntity<String>
  fun generateAndSaveAccessToken(user: User): String
  fun addAchievementToUser(userId: Long, achievementId: Long): ResponseEntity<Any>
  fun updateUserData(request: UserPatchRequestDTO, username: String): Boolean
  fun addToFavorite(user: User, release: Release): Boolean
  
  //  fun findByUsername(username: String): Optional<User>
//  fun existByUsername(username: String): Boolean
//  fun existsByEmail(email: String): Boolean
}
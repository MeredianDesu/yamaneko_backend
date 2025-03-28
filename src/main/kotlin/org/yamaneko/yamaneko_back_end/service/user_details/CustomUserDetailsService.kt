package org.yamaneko.yamaneko_back_end.service.user_details

import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.repository.UserRepository

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
  
  private val logger = LoggerFactory.getLogger(CustomUserDetailsService::class.java)
  
  override fun loadUserByUsername(userId: String?): UserDetails {
    val id = userId?.toLongOrNull() ?: throw UsernameNotFoundException("Некорректный ID пользователя: $userId")
    logger.info("User ID: $id")
    
    return userRepository.findById(id).orElseThrow { UsernameNotFoundException("Пользователь с ID $id не найден") }
      .also { logger.info("Пользователь с ID $id успешно загружен") }
  }
  
}
package org.yamaneko.yamaneko_back_end.service.achievement

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementRequestDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.UserAchievementDTO
import org.yamaneko.yamaneko_back_end.entity.Achievement
import org.yamaneko.yamaneko_back_end.mappers.AchievementMapper
import org.yamaneko.yamaneko_back_end.repository.AchievementRepository
import org.yamaneko.yamaneko_back_end.repository.UserAchievementRepository

@Service
class AchievementServiceImpl(
  private val achievementRepository: AchievementRepository,
  private val userAchievementRepository: UserAchievementRepository,
): AchievementService {
  
  private val achievementMapper: AchievementMapper = AchievementMapper()
  
  override fun getAllAchievements(): List<AchievementDTO>? {
    val achievements = achievementRepository.findAll()
    
    return achievements.map { achievementMapper.toAchievementDTO(it) }
  }
  
  override fun getUserAchievements(userId: Long): List<UserAchievementDTO>? {
    
    return userAchievementRepository.findByUserId(userId)
      .map { achievementMapper.toUserAchievementDTO(it) }
      .sortedByDescending { it.receivedAt }
  }
  
  override fun createAchievement(achievement: AchievementRequestDTO): AchievementDTO? {
    val newAchievement = Achievement().apply {
      image = achievement.image
      name = achievement.name
      condition = achievement.condition
    }
    
    val response = achievementRepository.save(newAchievement)
    
    return achievementMapper.toAchievementDTO(response)
  }
  
  override fun deleteAchievement(achievementId: Long): ResponseEntity<Any> {
    val achievement = achievementRepository.findById(achievementId)
    
    return if(achievement.isPresent) {
      achievementRepository.deleteById(achievementId)
      ResponseEntity.ok()
        .build()
    } else {
      ResponseEntity.notFound()
        .build()
    }
  }
}
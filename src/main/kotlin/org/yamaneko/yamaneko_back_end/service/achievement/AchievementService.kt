package org.yamaneko.yamaneko_back_end.service.achievement

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementRequestDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.UserAchievementDTO

interface AchievementService {
  
  fun getAllAchievements(): List<AchievementDTO>?
  fun getUserAchievements(userId: Long): List<UserAchievementDTO>?
  fun createAchievement(achievement: AchievementRequestDTO): AchievementDTO?
  fun deleteAchievement(achievementId: Long): ResponseEntity<Any>
}
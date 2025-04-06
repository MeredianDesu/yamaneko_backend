package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.yamaneko.yamaneko_back_end.dto.embeded.UserAchievementId
import org.yamaneko.yamaneko_back_end.entity.UserAchievement

interface UserAchievementRepository: JpaRepository<UserAchievement, UserAchievementId> {
  
  @Query("FROM UserAchievement u WHERE u.user.id = :userId")
  fun findByUserId(userId: Long): List<UserAchievement>
  
}
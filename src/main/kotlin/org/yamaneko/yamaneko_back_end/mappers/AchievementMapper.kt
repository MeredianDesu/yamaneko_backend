package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.UserAchievementDTO
import org.yamaneko.yamaneko_back_end.entity.Achievement
import org.yamaneko.yamaneko_back_end.entity.UserAchievement

class AchievementMapper {
  
  fun toUserAchievementDTO(userAchievement: UserAchievement): UserAchievementDTO {
    
    val achievement = userAchievement.achievement
    println(achievement?.id)
    println(achievement?.name)
    
    return if(achievement != null) {
      UserAchievementDTO(
        id = achievement.id,
        image = achievement.image,
        name = achievement.name,
        condition = achievement.condition,
        receivedAt = userAchievement.receivedAt,
      )
    } else {
      UserAchievementDTO(
        id = 0L, image = "N/A", name = "N/A", condition = "N/A", receivedAt = "N/A"
      )
    }
  }
  
  fun toAchievementDTO(achievements: Achievement): AchievementDTO {
    return AchievementDTO(
      id = achievements.id,
      image = achievements.image,
      name = achievements.name,
      condition = achievements.condition,
    )
  }
}
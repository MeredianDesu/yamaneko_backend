package org.yamaneko.yamaneko_back_end.entity

import jakarta.persistence.*
import org.yamaneko.yamaneko_back_end.dto.embeded.UserAchievementId

@Entity
@Table(name = "user_achievements")
open class UserAchievement(
  @EmbeddedId internal var id: UserAchievementId = UserAchievementId(),
  
  @ManyToOne @MapsId("userId") @JoinColumn(name = "user_id") internal var user: User? = null,
  
  @ManyToOne @MapsId("achievementId") @JoinColumn(name = "achievement_id") internal var achievement: Achievement? = null,
  
  @Column(name = "received_at", nullable = false) internal var receivedAt: String = "",
)
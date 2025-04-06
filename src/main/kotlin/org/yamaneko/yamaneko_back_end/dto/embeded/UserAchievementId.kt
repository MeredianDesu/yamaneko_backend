package org.yamaneko.yamaneko_back_end.dto.embeded

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserAchievementId(
  @Column(name = "user_id") var userId: Long = 0,
  @Column(name = "achievement_id") var achievementId: Long = 0,
): Serializable {}

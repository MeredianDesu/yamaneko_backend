package org.yamaneko.yamaneko_back_end.dto.achievement

data class UserAchievementDTO(
  val id: Long,
  val image: String,
  val name: String,
  val condition: String,
  val receivedAt: String,
)

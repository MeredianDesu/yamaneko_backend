package org.yamaneko.yamaneko_back_end.dto.achievement

data class AchievementDTO(
  val id: Long,
  val image: String,
  val name: String,
  val condition: String,
)

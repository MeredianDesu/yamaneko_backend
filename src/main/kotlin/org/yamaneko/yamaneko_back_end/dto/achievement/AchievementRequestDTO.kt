package org.yamaneko.yamaneko_back_end.dto.achievement

data class AchievementRequestDTO(
  val image: String,
  val name: String,
  val condition: String,
)

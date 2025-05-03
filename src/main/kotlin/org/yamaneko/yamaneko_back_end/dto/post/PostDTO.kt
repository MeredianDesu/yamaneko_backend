package org.yamaneko.yamaneko_back_end.dto.post

data class PostDTO(
  val id: Long, val text: String, val createdAt: String, val userId: Long?
)

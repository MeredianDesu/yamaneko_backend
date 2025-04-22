package org.yamaneko.yamaneko_back_end.dto.user

data class UserPatchRequestDTO(
  val header: String? = null,
  val avatar: String? = null,
)

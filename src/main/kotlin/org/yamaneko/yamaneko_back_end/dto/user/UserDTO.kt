package org.yamaneko.yamaneko_back_end.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yamaneko.yamaneko_back_end.entity.Role

data class UserDTO(
  @field:NotNull(message = "ID cannot be null") val id: Long,
  @field:NotBlank(message = "Username cannot be blank") val username: String,
  @field:NotNull(message = "Roles cannot be null") val roles: Role,
  val avatar: String,
  val header: String,
  @field:NotBlank(message = "Creation date cannot be null or blank") val createdAt: String
)

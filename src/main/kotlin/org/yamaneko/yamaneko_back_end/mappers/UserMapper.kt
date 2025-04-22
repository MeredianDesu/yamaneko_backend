package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.entity.User

class UserMapper {
  
  fun toDTO(user: User): UserDTO {
    return UserDTO(
      id = user.id,
      username = user.username,
      roles = user.roles,
      avatar = user.avatar ?: "",
      header = user.header ?: "",
      createdAt = user.createdAt,
    )
  }
}
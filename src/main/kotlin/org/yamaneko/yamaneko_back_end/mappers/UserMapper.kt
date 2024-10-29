package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.entity.User

class UserMapper {

    fun toDTO( user: User ): UserDTO {
        return UserDTO(
            id = user.id,
            username = user.username,
            email = user.email,
            password = user.password,
            roles = user.roles.toMutableSet(),
            avatar = user.avatar,
            createdAt = user.createdAt,
        )
    }
}
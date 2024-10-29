package org.yamaneko.yamaneko_back_end.service.user

import org.yamaneko.yamaneko_back_end.dto.user.UserDTO

interface UserService {
    fun getAllUsers(): List<UserDTO>
}
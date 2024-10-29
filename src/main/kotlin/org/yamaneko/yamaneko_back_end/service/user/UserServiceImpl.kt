package org.yamaneko.yamaneko_back_end.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository

@Service
class UserServiceImpl: UserService {

    private var userMapper = UserMapper()

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getAllUsers(): List<UserDTO> {
        val userList = userRepository.findAll()

        return userList.map { userMapper.toDTO( it ) }
    }
}
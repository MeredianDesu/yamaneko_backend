package org.yamaneko.yamaneko_back_end.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.entity.User
import org.yamaneko.yamaneko_back_end.entity.UserTokens
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRepository
import org.yamaneko.yamaneko_back_end.repository.UserTokensRepository
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import org.yamaneko.yamaneko_back_end.utils.JwtUtil
import java.util.*

@Service
class UserServiceImpl: UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userTokensRepository: UserTokensRepository

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var dateFormatter: DateFormatter

    private var userMapper = UserMapper()

    override fun getAllUsers(): List<UserDTO> {
        val userList = userRepository.findAll()

        return userList.map { userMapper.toDTO( it ) }
    }

    override fun registerUser( request: UserRegistrationRequest ): ResponseEntity<String> {
        val response = validateUserRequest( request )
        if( response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY )
            return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( "Password doesn't match" )
        if( response.statusCode == HttpStatus.CONFLICT )
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "A user with this email address is already registered" )

        val date = dateFormatter.dateToString( Date() )

        val user = User()
        user.username = request.username
        user.password = request.password
        user.email = request.email
        user.createdAt = date

        userRepository.save( user )

        val token = jwtUtil.generateToken( user )

        val userTokens = UserTokens()
        userTokens.user = user
        userTokens.tokenHash = jwtUtil.hashJwtToken( token )
        userTokens.createdAt = dateFormatter.dateToString( jwtUtil.extractCreation( token )!! )
        userTokens.expiresAt = dateFormatter.dateToString( jwtUtil.extractExpiration( token )!! )

        userTokensRepository.save( userTokens )

        return ResponseEntity.status( HttpStatus.CREATED ).body( token )
    }

    private fun validateUserRequest( request: UserRegistrationRequest ): ResponseEntity<String> {
        val userWithEmail = userRepository.findByEmail( request.email )

        if( request.password != request.repeatedPassword )
            return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( "Password doesn't match" )
        if( userWithEmail != null )
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "A user with this email address is already registered" )

        return ResponseEntity.status( HttpStatus.CONTINUE ).body( "Check passed successfully" )
    }
}
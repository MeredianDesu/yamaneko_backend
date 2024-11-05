package org.yamaneko.yamaneko_back_end.service.user

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.user.UserAuthRequest
import org.yamaneko.yamaneko_back_end.dto.user.UserDTO
import org.yamaneko.yamaneko_back_end.dto.user.UserRegistrationRequest
import org.yamaneko.yamaneko_back_end.entity.RefreshToken
import org.yamaneko.yamaneko_back_end.entity.User
import org.yamaneko.yamaneko_back_end.entity.UserToken
import org.yamaneko.yamaneko_back_end.mappers.UserMapper
import org.yamaneko.yamaneko_back_end.repository.UserRefreshTokensRepository
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
    lateinit var refreshTokensRepository: UserRefreshTokensRepository

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var dateFormatter: DateFormatter

    private var userMapper = UserMapper()

    override fun getAllUsers(): List<UserDTO> {
        val userList = userRepository.findAll()

        return userList.map { userMapper.toDTO( it ) }
    }

    // User registration
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
        val refreshToken = jwtUtil.generateRefreshToken()

        val userToken = UserToken()
        userToken.user = user
        userToken.tokenHash = jwtUtil.hashToken( token )
        userToken.createdAt = dateFormatter.dateToString( jwtUtil.extractCreation( token )!! )
        userToken.expiresAt = dateFormatter.dateToString( jwtUtil.extractExpiration( token )!! )

        val userRefreshToken = RefreshToken()
        userRefreshToken.user = user
        userRefreshToken.token = refreshToken["token"]!!
        userRefreshToken.createdAt = refreshToken["issuedAt"]!!
        userRefreshToken.expiresAt = refreshToken["expirationDate"]!!

        userTokensRepository.save( userToken )
        refreshTokensRepository.save( userRefreshToken )

        return ResponseEntity.status( HttpStatus.CREATED ).body( "$token|${refreshToken["token"]!!}" )
    }

    @Transactional
    override fun authUser( request: UserAuthRequest ): ResponseEntity<String> {
        val user = userRepository.findByEmail( request.email )
            ?: return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found" )

        if( user.email == request.email && user.password == request.password ) {
            val refreshToken = refreshTokensRepository.findByUser( user.id )?.token
            // If refresh token not expired
            if ( refreshToken != null ) {
//                userTokensRepository.disableToken( userTokensRepository.findByToken( user.id )?.tokenHash )
                val newAccessToken = generateAndSaveAccessToken( user )

                return ResponseEntity.status( HttpStatus.OK ).body( "$newAccessToken|$refreshToken")
            }
            // If refresh token expired
            else {
//                userTokensRepository.disableToken( userTokensRepository.findByToken( user )?.tokenHash )
//                refreshTokensRepository.disableToken( refreshTokensRepository.findByUser( user.id )?.token )
                val newAccessToken = generateAndSaveAccessToken( user )
                val newRefreshToken = generateAndSaveRefreshToken( user )

                return ResponseEntity.status( HttpStatus.OK ).body( "$newAccessToken|$newRefreshToken")
            }
        }
        else
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Invalid credentials" )
    }

    private fun generateAndSaveAccessToken( user: User ): String{
        val newAccessToken = jwtUtil.generateToken(user)
        val userToken = UserToken().apply {
            this.user = user
            this.tokenHash = jwtUtil.hashToken(newAccessToken)
            this.createdAt = dateFormatter.dateToString(jwtUtil.extractCreation(newAccessToken)!!)
            this.expiresAt = dateFormatter.dateToString(jwtUtil.extractExpiration(newAccessToken)!!)
        }
        userTokensRepository.save( userToken )

        return newAccessToken
    }

    private fun generateAndSaveRefreshToken( user: User ): String{
        val newRefreshToken = jwtUtil.generateRefreshToken()
        val refreshTokenInstance = RefreshToken().apply {
            this.user = user
            this.token = jwtUtil.hashToken(newRefreshToken["token"]!!)
            this.createdAt = newRefreshToken["issuedAt"]!!
            this.expiresAt = newRefreshToken["expirationDate"]!!
        }
        refreshTokensRepository.save(refreshTokenInstance)

        return newRefreshToken["token"]!!
    }

    // Validate input request
    private fun validateUserRequest( request: UserRegistrationRequest ): ResponseEntity<String> {
        val userWithEmail = userRepository.findByEmail( request.email )

        if( request.password != request.repeatedPassword )
            return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY ).body( "Password doesn't match" )
        if( userWithEmail != null )
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "A user with this email address is already registered" )

        return ResponseEntity.status( HttpStatus.CONTINUE ).body( "Check passed successfully" )
    }
}
package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.RefreshToken

interface UserRefreshTokensRepository: JpaRepository<RefreshToken, Long>{
    @Query( "FROM RefreshToken a WHERE a.user.id = :userId AND TO_TIMESTAMP( a.expiresAt, 'dd-MM-yy HH:mm:ss' ) > CURRENT_TIMESTAMP" )
    fun findByUser(@Param( "userId" ) userId: Long ): RefreshToken?

//    @Modifying
//    @Query("UPDATE RefreshToken a SET a.isRevoked = true WHERE a.token = :token")
//    fun disableToken( @Param( "token" ) token: String? )
}
package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.UserToken

interface UserTokensRepository: JpaRepository<UserToken, Long>{

    @Query("FROM UserToken a WHERE a.tokenHash = :token AND TO_TIMESTAMP( a.expiresAt, 'DD/MM/YYYY HH24:MI:SS' ) > CURRENT_TIMESTAMP")
    fun findByToken(@Param( "token" ) token: String ): UserToken?

    @Modifying
    @Query("UPDATE UserToken a SET a.isRevoked = true WHERE a.tokenHash = :tokenHash")
    fun disableToken( @Param( "tokenHash" ) tokenHash: String? )
}
package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.UserToken

interface UserTokensRepository: JpaRepository<UserToken, Long> {
  
  @Query(value = "SELECT * FROM user_tokens WHERE jwt_token = :jwtToken LIMIT 1", nativeQuery = true)
  fun findByToken(@Param("jwtToken") jwtToken: String): UserToken?
  
  
  @Modifying
  @Query("UPDATE UserToken a SET a.isRevoked = true WHERE a.jwtToken = :jwtToken")
  fun disableToken(@Param("jwtToken") jwtToken: String?)
}
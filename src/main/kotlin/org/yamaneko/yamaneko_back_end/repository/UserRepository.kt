package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.User

interface UserRepository: JpaRepository<User, Long> {
  
  @Query("FROM User u WHERE u.email = :email")
  fun findByEmail(@Param("email") email: String): User?
  
  @Query("FROM User u WHERE u.username = :username")
  fun findByUsername(@Param("username") username: String): User?
}
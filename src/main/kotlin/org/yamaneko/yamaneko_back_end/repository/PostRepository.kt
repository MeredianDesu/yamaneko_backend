package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.Post

interface PostRepository: JpaRepository<Post, Long> {
  
  @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id = :userId")
  fun findByUserId(@Param("userId") userId: Long): List<Post>?
  
}
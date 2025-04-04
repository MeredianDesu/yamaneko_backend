package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.Team

interface TeamRepository: JpaRepository<Team, Long> {
  
  @Query("FROM Team t WHERE t.user.id = :userId")
  fun findByUserId(@Param("userId") userId: Long): Team?
  
  @Modifying(clearAutomatically = true)
  @Query("DELETE FROM Team t WHERE t.user.id IN (:userId)")
  fun deleteByUserId(@Param("userId") userId: Long)
}
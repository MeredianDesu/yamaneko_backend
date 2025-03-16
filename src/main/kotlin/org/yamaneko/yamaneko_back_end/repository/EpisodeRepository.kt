package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.yamaneko.yamaneko_back_end.entity.Episode

interface EpisodeRepository: JpaRepository<Episode, Long> {
    @Query( value = "FROM Episode e WHERE e.release.id = :releaseId" )
    fun findAllEpisodesByReleaseId( releaseId: Long ): List<Episode>
}
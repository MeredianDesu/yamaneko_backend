package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yamaneko.yamaneko_back_end.entity.EpisodeQuality

interface EpisodeQualitiesRepository: JpaRepository<EpisodeQuality, Long> {
}
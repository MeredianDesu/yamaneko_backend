package org.yamaneko.yamaneko_back_end.service.episode

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.repository.EpisodeRepository

@Service
class EpisodeServiceImpl: EpisodeService {
    @Autowired private lateinit var episodeRepository: EpisodeRepository

    override fun getEpisodesNumber( releaseId: Long ): Long {

        return episodeRepository.findAllEpisodesByReleaseId( releaseId ).count().toLong()
    }
}
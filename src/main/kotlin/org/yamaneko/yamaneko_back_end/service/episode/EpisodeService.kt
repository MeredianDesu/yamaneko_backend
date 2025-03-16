package org.yamaneko.yamaneko_back_end.service.episode

interface EpisodeService {
    fun getEpisodesNumber( releaseId: Long ): Long
}
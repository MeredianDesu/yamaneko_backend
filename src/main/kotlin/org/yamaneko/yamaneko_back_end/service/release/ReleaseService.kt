package org.yamaneko.yamaneko_back_end.service.release

import org.springframework.http.ResponseEntity
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost

interface ReleaseService {
    fun getAllReleases(): List<ReleaseDTO>
    fun getLatestReleases( length: Int ): List<ReleaseDTO>
    fun getRelease( id: Long ): ReleaseDTO?
    fun createRelease( request: ReleaseRequestPost ): ReleaseDTO
    fun removeRelease( id: Long ): ResponseEntity<String>
}
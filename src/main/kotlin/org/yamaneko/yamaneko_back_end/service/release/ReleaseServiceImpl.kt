package org.yamaneko.yamaneko_back_end.service.release

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost
import org.yamaneko.yamaneko_back_end.entity.Release
import org.yamaneko.yamaneko_back_end.mappers.ReleaseMapper
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository
import org.yamaneko.yamaneko_back_end.repository.GenreRepository
import org.yamaneko.yamaneko_back_end.repository.ReleaseRepository
import org.yamaneko.yamaneko_back_end.repository.TeamRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReleaseServiceImpl: ReleaseService {

    @Autowired
    private lateinit var characterRepository: CharacterRepository

    @Autowired
    private lateinit var genreRepository: GenreRepository

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var releaseRepository: ReleaseRepository

    private val releaseMapper = ReleaseMapper()

    override fun getAllReleases(): List<ReleaseDTO> {
        val releases = releaseRepository.findAll()

        return releases.map{ releaseMapper.toDTO( it ) }
    }

    override fun getLatestReleases( length: Int ): List<ReleaseDTO> {

        return releaseRepository.findLastReleases( length ).map{ releaseMapper.toDTO( it ) }
    }

    override fun getRelease( id: Long ): ReleaseDTO? {
        val release = releaseRepository.findReleaseById( id )

        return if( release != null )
            releaseMapper.toDTO( release )
        else
            null
    }

    override fun createRelease( request: ReleaseRequestPost ): ReleaseDTO {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = currentDate.format( formatter )

        val genresId = request.genres
        val genresList = genreRepository.findAllById( genresId )

        val release = Release().apply {
            originalName = request.originalName
            translatedName = request.translatedName
            posterImageUrl = request.posterImageUrl ?: PREVIEW_IMG_URL
            previewVideoUrl = request.previewVideoUrl ?: PREVIEW_VIDEO_URL
            videoUrl = request.videoUrl
            sinopsis = request.sinopsis
            info = request.info
            dubbers = request.dubbers.flatMap { role ->
                val charactersList = characterRepository.findAllById( role.charactersId )
                charactersList.map{ character ->
                    RolesDTO(
                        characterId = character.id,
                        characterOriginalName = character.originalName,
                        characterTranslatedName = character.translatedName,
                        characterImage = character.image,
                        dubberName = teamRepository.findById( role. dubberId ).get().name
                    )
                }
            }.toMutableList()
            genres = genresList.toMutableSet()
            uploadedAt = date
        }

        val savedRelease = releaseRepository.save( release )

        return releaseMapper.toDTO( savedRelease )
    }

    companion object{
        private const val PREVIEW_VIDEO_URL = "var/yamaneko_files/preview.mp4"
        private const val PREVIEW_IMG_URL = "var/yamaneko_files/mascot.jfif"
    }
}

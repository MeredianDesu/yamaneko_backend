package org.yamaneko.yamaneko_back_end.service.release

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
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

    @Value("\${yamaneko.server}")
    private lateinit var filesServerUrl: String

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
            posterImageUrl = "${filesServerUrl}${request.posterImageUrl}"
            previewVideoUrl = "${filesServerUrl}${request.previewVideoUrl}"
            videoUrl = "${filesServerUrl}${request.videoUrl}"
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

    override fun removeRelease( id: Long ): ResponseEntity<String> {
        val release = releaseRepository.findReleaseById( id )

        return if ( release != null ) {
            releaseRepository.deleteById(release.id)
            ResponseEntity.status( HttpStatus.OK ).body( "Release deleted" )
        }
        else
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Release not found" )

    }
}

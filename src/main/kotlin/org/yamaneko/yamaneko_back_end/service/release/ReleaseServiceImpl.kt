package org.yamaneko.yamaneko_back_end.service.release

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.RolesDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPatch
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost
import org.yamaneko.yamaneko_back_end.entity.Release
import org.yamaneko.yamaneko_back_end.mappers.ReleaseMapper
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository
import org.yamaneko.yamaneko_back_end.repository.GenreRepository
import org.yamaneko.yamaneko_back_end.repository.ReleaseRepository
import org.yamaneko.yamaneko_back_end.repository.TeamRepository
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import java.util.Date

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

    private val dateFormatter = DateFormatter()

    override fun getAllReleases(): List<ReleaseDTO> {
        val releases = releaseRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))

        return releases.map{ releaseMapper.toDTO( it ) }
    }

    override fun getLatestReleases( length: Int ): List<ReleaseDTO> {

        return releaseRepository.findLastReleases( length ).map{ releaseMapper.toDTO( it ) }
    }

    override fun getReleaseById(id: Long ): ReleaseDTO? {
        val release = releaseRepository.findReleaseById( id )

        return if( release != null )
            releaseMapper.toDTO( release )
        else
            null
    }

    override fun createRelease( request: ReleaseRequestPost ): ReleaseDTO {
        val date = dateFormatter.dateToString( Date() )

        val genresId = request.genres
        val genresList = genreRepository.findAllById( genresId )

        val release = Release().apply {
            originalName = request.originalName
            translatedName = request.translatedName
            posterImageUrl = request.posterImageUrl.takeIf { !it.isNullOrEmpty() } ?: "${filesServerUrl}${request.posterImageUrl}"
            previewVideoUrl = request.previewVideoUrl.takeIf{ !it.isNullOrEmpty() } ?: "${filesServerUrl}${request.previewVideoUrl}"
            videoUrl = request.videoUrl.takeIf{ !it.isNullOrEmpty() } ?: "${filesServerUrl}${request.videoUrl}"
            sinopsis = request.synopsis
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

    override fun getReleaseByKeyword( keyword: String ): ResponseEntity<Any> {
        val release = releaseRepository.findReleaseByName( "%$keyword%" ) ?: return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Release not found" )
        val response = releaseMapper.toDTO( release )

        return ResponseEntity.status( HttpStatus.OK ).body( response )
    }

    override fun getReleasesByGenre( genre: String ): ResponseEntity<Any> {
        val releases = releaseRepository.findReleasesByGenre( "%$genre%" ) ?: return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Releases not found" )

        val response = releases.map{ releaseMapper.toDTO( it ) }

        return ResponseEntity.status( HttpStatus.OK ).body( response )
    }

    override fun updateRelease(request: ReleaseRequestPatch, releaseId: Long ): ReleaseDTO {
        val release = releaseRepository.findById( releaseId ).orElseThrow { NoSuchElementException("Release not found") }

        request.originalName?.let{ release.originalName = it }
        request.translatedName?.let{ release.translatedName = it }
        request.posterImageUrl?.let{ release.posterImageUrl = it }
        request.previewImageUrl?.let{ release.previewVideoUrl = it }
        request.videoUrl?.let{ release.videoUrl = it }
        request.synopsis?.let{ release.sinopsis = it }
        request.info?.let{ release.info = it }

        releaseRepository.save( release )

        return releaseMapper.toDTO( release )
    }
}

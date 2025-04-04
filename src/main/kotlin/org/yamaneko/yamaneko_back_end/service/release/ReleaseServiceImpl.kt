package org.yamaneko.yamaneko_back_end.service.release

import org.slf4j.LoggerFactory
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
import org.yamaneko.yamaneko_back_end.mappers.EpisodeMapper
import org.yamaneko.yamaneko_back_end.mappers.ReleaseMapper
import org.yamaneko.yamaneko_back_end.repository.CharacterRepository
import org.yamaneko.yamaneko_back_end.repository.GenreRepository
import org.yamaneko.yamaneko_back_end.repository.ReleaseRepository
import org.yamaneko.yamaneko_back_end.repository.TeamRepository
import org.yamaneko.yamaneko_back_end.service.episode.EpisodeService
import org.yamaneko.yamaneko_back_end.utils.DateFormatter
import java.util.*

@Service
class ReleaseServiceImpl: ReleaseService {
  
  val logger = LoggerFactory.getLogger(ReleaseServiceImpl::class.java)
  
  @Autowired
  private lateinit var characterRepository: CharacterRepository
  
  @Autowired
  private lateinit var genreRepository: GenreRepository
  
  @Autowired
  private lateinit var teamRepository: TeamRepository
  
  @Autowired
  private lateinit var releaseRepository: ReleaseRepository
  
  @Autowired
  private lateinit var episodeService: EpisodeService
  
  @Value("\${yamaneko.server}")
  private lateinit var filesServerUrl: String
  
  private val releaseMapper = ReleaseMapper()
  
  private val dateFormatter = DateFormatter()
  
  private val episodeMapper = EpisodeMapper()
  
  override fun getAllReleases(): List<ReleaseDTO> {
    val releases = releaseRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
    
    return releases.map { releaseMapper.toDTO(it) }
  }
  
  override fun getLatestReleases(length: Int): List<ReleaseDTO> {
    if(length <= 0) {
      throw IllegalArgumentException("Length must be greater than 0")
    }
    
    return try {
      releaseRepository.findLastReleases(length).map { releaseMapper.toDTO(it) }
    } catch(e: Exception) {
      throw RuntimeException("Error fetching latest releases", e)
    }
  }
  
  override fun getReleaseById(id: Long): ReleaseDTO? {
    val release = releaseRepository.findReleaseById(id)
    
    return if(release != null) releaseMapper.toDTO(release)
    else null
  }
  
  override fun createRelease(request: ReleaseRequestPost): ReleaseDTO {
    val date = dateFormatter.dateToString(Date())
    
    val genresId = request.genres
    val genresList = genreRepository.findAllById(genresId)
    
    val release = Release().apply {
      originalName = request.originalName
      translatedName = request.translatedName
      ageRestriction = request.ageRestriction
      maxEpisodes = request.maxEpisodes
      status = request.status
      posterImageUrl =
        request.posterImageUrl.takeIf { ! it.isNullOrEmpty() } ?: "${filesServerUrl}${request.posterImageUrl}"
      previewVideoUrl =
        request.previewVideoUrl.takeIf { ! it.isNullOrEmpty() } ?: "${filesServerUrl}${request.previewVideoUrl}"
//            episodes = request.episodes.map { episodeDTO ->
//                episodeMapper.toEntity( episodeDTO, this )
//            }.toMutableList()
      synopsis = request.synopsis
      info = request.info
      dubbers = request.dubbers.flatMap { role ->
        val charactersList = characterRepository.findAllById(role.charactersId)
        charactersList.map { character ->
          RolesDTO(
            characterId = character.id,
            characterOriginalName = character.originalName,
            characterTranslatedName = character.translatedName,
            characterImage = character.image,
            dubberName = teamRepository.findById(role.dubberId).get().name
          )
        }
      }.toMutableList()
      genres = genresList.toMutableSet()
      updatedAt = date
      uploadedAt = date
    }
    
    val savedRelease = releaseRepository.save(release)
    
    return releaseMapper.toDTO(savedRelease)
  }
  
  override fun removeRelease(id: Long): ResponseEntity<String> {
    val release = releaseRepository.findReleaseById(id)
    
    return if(release != null) {
      releaseRepository.deleteById(release.id)
      ResponseEntity.status(HttpStatus.OK).body("Release deleted")
    } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Release not found")
    
  }
  
  override fun getReleasesByTitleQuery(query: String): List<ReleaseDTO>? {
    val isQuery = releaseRepository.findByTitleQuery(query).isNullOrEmpty()
    
    return if(! isQuery) {
      val releases = releaseRepository.findByTitleQuery(query)
      releases?.map { releaseMapper.toDTO(it) }
    } else {
      null
    }
  }
  
  override fun updateRelease(request: ReleaseRequestPatch, releaseId: Long): ReleaseDTO {
    val release = releaseRepository.findById(releaseId).orElseThrow { NoSuchElementException("Release not found") }
    
    request.originalName?.let { release.originalName = it }
    request.translatedName?.let { release.translatedName = it }
    request.ageRestriction?.let { release.ageRestriction = it }
    request.maxEpisodes?.let { release.maxEpisodes = it }
    request.status?.let { release.status = it }
    request.posterImageUrl?.let { release.posterImageUrl = it }
    request.previewVideoUrl?.let { release.previewVideoUrl = it }
    request.episodes?.let { newEpisodes ->
      val existingEpisodes = release.episodes.toMutableList()
      val episodeNumber = episodeService.getEpisodesNumber(releaseId) + 1
      val newEpisodeEntities = newEpisodes.map { episodeDTO ->
        episodeMapper.toEntity(episodeDTO, release, episodeNumber) // Передаём релиз в метод
      }
      existingEpisodes.addAll(newEpisodeEntities)
      release.episodes = existingEpisodes
    }
    request.synopsis?.let { release.synopsis = it }
    request.info?.let { release.info = it }
    release.updatedAt = dateFormatter.dateToString(Date())
    
    releaseRepository.save(release)
    
    return releaseMapper.toDTO(release)
  }
}

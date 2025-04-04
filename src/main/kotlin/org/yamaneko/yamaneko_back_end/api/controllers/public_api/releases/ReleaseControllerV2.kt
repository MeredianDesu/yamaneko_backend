package org.yamaneko.yamaneko_back_end.api.controllers.public_api.releases

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.bot_dto.BotRequestDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPatch
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost
import org.yamaneko.yamaneko_back_end.repository.ReleaseRepository
import org.yamaneko.yamaneko_back_end.service.discord_bot.BotService
import org.yamaneko.yamaneko_back_end.service.release.ReleaseService
import org.yamaneko.yamaneko_back_end.utils.AuthenticatedUserData

@Tag(name = "{ V2 } Releases API")
@RestController
@RequestMapping("/api/releases/v2")
class ReleaseControllerV2(
  private val releaseService: ReleaseService,
  private val botService: BotService,
  private val releaseRepository: ReleaseRepository,
) {
  
  val logger = LoggerFactory.getLogger(ReleaseControllerV2::class.java)
  
  private val userData = AuthenticatedUserData()
  
  @Operation(summary = "Get all releases from DB.")
  @GetMapping("")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
      ),
    ]
  )
  fun getReleases(
    @Parameter(
      description = "Number of releases received.", example = "1"
    ) @RequestParam(required = false) length: Int?
  ): ResponseEntity<List<ReleaseDTO>> {
    val releases = if(length != null) releaseService.getLatestReleases(length)
    else releaseService.getAllReleases()
    
    return if(releases.isNotEmpty()) ResponseEntity.status(HttpStatus.OK).body(releases)
    else ResponseEntity.noContent().build()
  }
  
  @Operation(summary = "Get release by ID.")
  @GetMapping("{releaseId}")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
      ),
    ]
  )
  fun getReleaseById(
    @Parameter(
      description = "ID of release.", example = "1"
    ) @PathVariable(required = true) releaseId: Long
  ): ResponseEntity<ReleaseDTO> {
    val release = releaseService.getReleaseById(releaseId)
    
    return if(release != null) ResponseEntity.status(HttpStatus.OK).body(release)
    else ResponseEntity.notFound().build()
  }
  
  @Operation(summary = "Create new release.")
  @PostMapping("")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Release created",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
      ),
    ]
  )
  fun saveRelease(
    @Valid @RequestBody request: ReleaseRequestPost, httpRequest: HttpServletRequest
  ): ResponseEntity<ReleaseDTO> {
    val response = releaseService.createRelease(request)
    
    val botNotificationBody = BotRequestDTO(
      id = response.id,
      name = request.translatedName,
      timestamp = response.uploadedAt,
      
      user = userData.getAuthenticatedUser(),
      address = userData.getClientIpAddress(httpRequest),
      method = httpRequest.method,
    )
    botService.createReleaseNotification(botNotificationBody)
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response)
  }
  
  @Operation(summary = "Delete release")
  @DeleteMapping("{releaseId}")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Release deleted",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Release deleted"))]
      ),
    ]
  )
  fun removeRelease(@Parameter(description = "Release ID") @PathVariable(required = true) releaseId: Long): ResponseEntity<String> {
    val status = releaseService.removeRelease(releaseId)
    
    return if(status.statusCode == HttpStatus.OK) ResponseEntity.ok().build()
    else ResponseEntity.notFound().build()
  }
  
  @Operation(summary = "Update release.")
  @PatchMapping("/{releaseId}")
  fun patchRelease(
    @RequestBody request: ReleaseRequestPatch, @PathVariable releaseId: Long, httpRequest: HttpServletRequest
  ): ResponseEntity<Any> {
    val releaseName = releaseRepository.findReleaseById(releaseId)?.translatedName
    
    val updatedRelease = releaseService.updateRelease(request, releaseId)
    
    val botNotificationBody = BotRequestDTO(
      id = updatedRelease.id,
      name = releaseName,
      timestamp = updatedRelease.uploadedAt,
      
      user = userData.getAuthenticatedUser(),
      address = userData.getClientIpAddress(httpRequest),
      method = httpRequest.method,
    )
    botService.createReleaseNotification(botNotificationBody)
    
    
    return ResponseEntity.ok().build()
  }
  
  @Operation(summary = "Get releases by title searching.")
  @GetMapping("/search")
  @ApiResponses(
    value = [ApiResponse(
      responseCode = "200",
      description = "Release(-es) founded",
      content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
    )]
  )
  fun getReleasesByTitleSearching(@RequestParam query: String): ResponseEntity<Any> {
    val releases = releaseService.getReleasesByTitleQuery(query)
    
    return if(releases.isNullOrEmpty()) {
      ResponseEntity.notFound().build()
    } else {
      ResponseEntity.ok().body(releases)
    }
  }
}
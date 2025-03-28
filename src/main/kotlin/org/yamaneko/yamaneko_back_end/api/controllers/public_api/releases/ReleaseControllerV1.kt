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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.ErrorResponse
import org.yamaneko.yamaneko_back_end.dto.bot_dto.BotRequestDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPatch
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost
import org.yamaneko.yamaneko_back_end.repository.ReleaseRepository
import org.yamaneko.yamaneko_back_end.service.discord_bot.BotService
import org.yamaneko.yamaneko_back_end.service.release.ReleaseService
import org.yamaneko.yamaneko_back_end.utils.AuthenticatedUserData

@Tag(name = "{ v1 } Releases API")
@RestController
@RequestMapping("/api/releases/v1")
@Deprecated(message = "Old controller")
class ReleaseControllerV1(
  private val releaseService: ReleaseService,
  private val botService: BotService,
  private val releaseRepository: ReleaseRepository
) {
  
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
      ApiResponse(
        responseCode = "203",
        description = "No content",
        content = [Content(
          mediaType = "text/plain",
          schema = Schema(type = "string", example = "No releases added yet")
        )]
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Bad request"))]
      ),
    ]
  )
  fun getReleases(
    @Parameter(
      description = "Number of releases received.",
      example = "4"
    ) @RequestParam(required = false) length: Int?
  ): ResponseEntity<List<ReleaseDTO>> {
    val releases = if(length != null) releaseService.getLatestReleases(length)
    else releaseService.getAllReleases()
    
    return if(releases.isNotEmpty()) ResponseEntity.status(HttpStatus.OK).body(releases)
    else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
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
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Bad request"))]
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = "text/plain", schema = Schema(implementation = ErrorResponse::class))]
      ),
    ]
  )
  fun getReleaseById(
    @Parameter(
      description = "ID of release.",
      example = "4"
    ) @PathVariable(required = false) releaseId: Long
  ): ResponseEntity<ReleaseDTO> {
    val release = releaseService.getReleaseById(releaseId)
    
    return if(release != null) ResponseEntity.status(HttpStatus.OK).body(release)
    else ResponseEntity.notFound().build()
  }
  
  @Operation(summary = "Find release by specified word by name.")
  @GetMapping("/search/name/{keyword}")
  @ApiResponses(
    value = [ApiResponse(
      responseCode = "200",
      description = "Release found",
      content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
    ), ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Release not found"))]
    )]
  )
  fun getReleaseByName(@Parameter(description = "Enter specified word for search.") @PathVariable(required = true) keyword: String): ResponseEntity<Any> {
    val response = releaseService.getReleaseByKeyword(keyword)
    
    return if(response?.statusCode == HttpStatus.NOT_FOUND) ResponseEntity.notFound().build()
    else ResponseEntity.status(HttpStatus.OK).body(response)
  }
  
  @Operation(summary = "Find release by specified word by name.")
  @GetMapping("/search/genre/{genre}")
  @ApiResponses(
    value = [ApiResponse(
      responseCode = "200",
      description = "Release found",
      content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
    ), ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Release not found"))]
    )]
  )
  fun getReleasesByGenre(@Parameter(description = "Enter specified genres for search.") @PathVariable(required = true) genre: String): ResponseEntity<Any> {
    val response = releaseService.getReleasesByGenre(genre)
    
    return if(response?.statusCode == HttpStatus.NOT_FOUND) ResponseEntity.notFound().build()
    else ResponseEntity.status(HttpStatus.OK).body(response)
  }
  
  @Operation(summary = "Create new release.")
  @PostMapping("")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "201",
        description = "Release created",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ReleaseDTO::class))]
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Bad request"))]
      ),
    ]
  )
  fun saveRelease(
    @Valid @RequestBody request: ReleaseRequestPost,
    httpRequest: HttpServletRequest
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
    value = [ApiResponse(
      responseCode = "200",
      description = "Release deleted",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Release deleted"))]
    ), ApiResponse(
      responseCode = "400",
      description = "Bad request",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Bad request"))]
    ), ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "Release not found"))]
    )]
  )
  fun removeRelease(@Parameter(description = "Release ID") @PathVariable(required = true) releaseId: Long): ResponseEntity<String> {
    val status = releaseService.removeRelease(releaseId)
    
    return if(status.statusCode == HttpStatus.OK) ResponseEntity.ok().build()
    else ResponseEntity.notFound().build()
  }
  
  @Operation(summary = "Update release.")
  @PatchMapping("/{releaseId}")
  fun patchRelease(
    @RequestBody request: ReleaseRequestPatch,
    @PathVariable releaseId: Long,
    httpRequest: HttpServletRequest
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
}
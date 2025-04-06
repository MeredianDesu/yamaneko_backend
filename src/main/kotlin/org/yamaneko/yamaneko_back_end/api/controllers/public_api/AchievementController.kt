package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.achievement.AchievementRequestDTO
import org.yamaneko.yamaneko_back_end.dto.achievement.UserAchievementDTO
import org.yamaneko.yamaneko_back_end.service.achievement.AchievementService

@Tag(name = "{ v1 } Achievements API")
@RestController
@RequestMapping("/api/achievements/v1")
class AchievementController(
  private val achievementService: AchievementService
) {
  
  private val logger = LoggerFactory.getLogger(AchievementController::class.java)
  
  @Operation(summary = "Get existing achievements")
  @GetMapping("")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserAchievementDTO::class))]
      ),
    ]
  )
  fun getAchievements(): ResponseEntity<Any> {
    val achievements = achievementService.getAllAchievements()
    logger.info("Achievements: {}", achievements)
    
    return if(achievements.isNullOrEmpty()) {
      ResponseEntity.notFound().build()
    } else {
      ResponseEntity.ok().body(achievements)
    }
  }
  
  @Operation(summary = "Get achievements by user id.")
  @GetMapping("{userId}")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserAchievementDTO::class))]
      ),
    ]
  )
  fun getUserAchievements(@PathVariable("userId") userId: Long): ResponseEntity<Any> {
    val achievements = achievementService.getUserAchievements(userId)
    
    return if(achievements.isNullOrEmpty()) {
      ResponseEntity.notFound().build()
    } else {
      ResponseEntity.ok().body(achievements)
    }
  }
  
  @Operation(summary = "Create achievement.")
  @PostMapping("")
  fun createAchievement(@RequestBody achievement: AchievementRequestDTO): ResponseEntity<Any> {
    val response = achievementService.createAchievement(achievement)
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response)
  }
  
  @Operation(summary = "Delete achievement.")
  @DeleteMapping("{achievementId}")
  fun deleteAchievement(@PathVariable("achievementId") achievementId: Long): ResponseEntity<Any> {
    
    return achievementService.deleteAchievement(achievementId)
  }
}
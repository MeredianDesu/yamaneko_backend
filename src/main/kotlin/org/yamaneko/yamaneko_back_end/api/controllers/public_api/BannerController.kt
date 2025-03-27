package org.yamaneko.yamaneko_back_end.api.controllers.public_api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.banner.BannerDTO
import org.yamaneko.yamaneko_back_end.dto.banner.BannerRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Banner
import org.yamaneko.yamaneko_back_end.repository.BannerRepository
import org.yamaneko.yamaneko_back_end.service.banner.BannerService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Tag(name = "{ v1 } Banners API")
@RestController
@RequestMapping("api/banners/v1")
class BannerController(
  @Autowired val bannerService: BannerService, @Autowired val bannerRepository: BannerRepository
) {
  
  @Operation(summary = "Get banners by visibility.")
  @ApiResponses(
    value = [ApiResponse(
      responseCode = "200",
      description = "Banners Returned",
      content = [Content(mediaType = "application/json", schema = Schema(implementation = BannerDTO::class))]
    ), ApiResponse(
      responseCode = "203",
      description = "No content",
      content = [Content(mediaType = "text/plain", schema = Schema(type = "string", example = "No content"))]
    )]
  )
  @GetMapping("")
  fun getBanners(
    @Parameter(description = "Fetch advertisements by visibility.", example = "true") @RequestParam(
      required = false
    ) visible: Boolean?
  ): ResponseEntity<List<BannerDTO>> {
    
    return when(visible) {
      true -> {
        val banners = bannerService.getAvailableAdvertisements()
        if(banners.isNotEmpty()) ResponseEntity.status(HttpStatus.OK).body(banners)
        else ResponseEntity.status(HttpStatus.NO_CONTENT).body(banners)
      }
      
      false -> {
        val banners = bannerService.getHiddenAdvertisements()
        if(banners.isNotEmpty()) ResponseEntity.status(HttpStatus.OK).body(banners)
        else ResponseEntity.status(HttpStatus.NO_CONTENT).body(banners)
      }
      
      else -> {
        val banners = bannerService.getAllAdvertisements()
        if(banners.isNotEmpty()) ResponseEntity.status(HttpStatus.OK).body(banners)
        else ResponseEntity.status(HttpStatus.NO_CONTENT).body(banners)
      }
    }
  }
  
  @Operation(summary = "Add banner.")
  @PostMapping("")
  fun createBanner(@Valid @RequestBody request: BannerRequestDTO): ResponseEntity<BannerDTO> {
    val currentDate = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val date = currentDate.format(formatter)
    
    val banner = Banner()
    banner.bannerImg = request.bannerImg
    banner.posterLink = request.posterLink
    banner.visible = request.visible
    banner.createdAt = date
    
    val savedBanner = bannerRepository.save(banner)
    val response = BannerDTO(
      id = savedBanner.id,
      bannerImg = savedBanner.bannerImg,
      posterLink = savedBanner.posterLink,
      visible = savedBanner.visible,
      createdAt = savedBanner.createdAt,
    )
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response)
  }
}
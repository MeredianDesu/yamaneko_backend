package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yamaneko.yamaneko_back_end.dto.banner.BannerDTO
import org.yamaneko.yamaneko_back_end.dto.banner.BannerRequestDTO
import org.yamaneko.yamaneko_back_end.entity.Banner
import org.yamaneko.yamaneko_back_end.mappers.BannerMapper
import org.yamaneko.yamaneko_back_end.repository.BannerRepository
import org.yamaneko.yamaneko_back_end.service.banner.BannerService
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping( "api/v1/banners" )
class BannerController(
    @Autowired val bannerService: BannerService,
    @Autowired val bannerRepository: BannerRepository
) {

    private val bannerMapper = BannerMapper()

    @Operation( summary = "Get banners by visibility." )
    @GetMapping("")
    fun getBanners( @Parameter( description = "Fetch advertisements by visibility.", example = "true" ) @RequestParam( required = false ) visible: Boolean? ): List<BannerDTO> {

        return when( visible ){
            true -> bannerService.getAvailableAdvertisements()
            false -> bannerService.getHiddenAdvertisements()
            else -> bannerService.getAllAdvertisements()
        }
    }

    @Operation( summary = "Add banner." )
    @PostMapping("")
    fun createBanner( @Valid @RequestBody request: BannerRequestDTO ): ResponseEntity<BannerDTO> {
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = currentDate.format( formatter )

        val banner = Banner()
        banner.bannerImg = request.bannerImg
        banner.posterLink = request.posterLink
        banner.visible = request.visible
        banner.createdAt = date

        val savedBanner = bannerRepository.save( banner )
        val response = BannerDTO(
            id = savedBanner.id,
            bannerImg = savedBanner.bannerImg,
            posterLink = savedBanner.posterLink,
            visible = savedBanner.visible,
            createdAt = savedBanner.createdAt,
        )

        return ResponseEntity.ok( response )
    }
}
package org.yamaneko.yamaneko_back_end.dto.banner

import jakarta.validation.constraints.NotBlank

data class BannerRequestDTO(
    @field:NotBlank( message = "Banner image link is blank" ) val bannerImg: String,
    @field:NotBlank( message = "Poster link is blank" ) val posterLink: String,
    val visible: Boolean
)

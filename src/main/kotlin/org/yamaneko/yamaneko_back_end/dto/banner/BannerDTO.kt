package org.yamaneko.yamaneko_back_end.dto.banner

import jakarta.validation.constraints.NotNull

data class BannerDTO(
    @field:NotNull( message = "ID cannot be null" ) val id: Long,
    val bannerImg: String,
    val posterLink: String,
    val visible: Boolean,
    val createdAt: String
)
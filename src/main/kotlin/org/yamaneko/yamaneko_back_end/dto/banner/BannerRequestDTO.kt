package org.yamaneko.yamaneko_back_end.dto.banner

import jakarta.validation.constraints.NotNull

data class BannerRequestDTO(
    val bannerImg: String,
    val posterLink: String,
    val visible: Boolean
)

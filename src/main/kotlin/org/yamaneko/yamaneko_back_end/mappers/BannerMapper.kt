package org.yamaneko.yamaneko_back_end.mappers

import org.yamaneko.yamaneko_back_end.dto.banner.BannerDTO
import org.yamaneko.yamaneko_back_end.entity.Banner

class BannerMapper {

    fun toDTO( banner: Banner ): BannerDTO {
        return BannerDTO(
            id = banner.id,
            bannerImg = banner.bannerImg,
            posterLink = banner.posterLink,
            visible = banner.visible,
            createdAt = banner.createdAt,
        )
    }
}
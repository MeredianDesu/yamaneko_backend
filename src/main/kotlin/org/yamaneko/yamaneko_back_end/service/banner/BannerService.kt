package org.yamaneko.yamaneko_back_end.service.banner

import org.springframework.data.jpa.repository.JpaRepository
import org.yamaneko.yamaneko_back_end.dto.banner.BannerDTO
import org.yamaneko.yamaneko_back_end.entity.Banner

// Banner service interface
interface BannerService{
    fun getAllAdvertisements(): List<BannerDTO>
    fun getAvailableAdvertisements(): List<BannerDTO>
    fun getHiddenAdvertisements(): List<BannerDTO>
}
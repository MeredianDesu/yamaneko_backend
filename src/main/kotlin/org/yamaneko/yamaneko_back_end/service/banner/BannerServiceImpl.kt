package org.yamaneko.yamaneko_back_end.service.banner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.dto.banner.BannerDTO
import org.yamaneko.yamaneko_back_end.mappers.BannerMapper
import org.yamaneko.yamaneko_back_end.repository.BannerRepository

// Functions implementations of BannerService
@Service
class BannerServiceImpl: BannerService {

    // Mapper
    private val advMapper = BannerMapper()

    // Repository
    @Autowired
    private lateinit var bannerRepository: BannerRepository

    // Return all advertisements
    override fun getAllAdvertisements(): List<BannerDTO> {
        val adv = bannerRepository.findAll()

        return adv.map { advMapper.toDTO( it ) }
    }

    // Return all visible advertisements
    override fun getAvailableAdvertisements(): List<BannerDTO> {
        val adv = bannerRepository.findVisibleAdvertisements()

        return adv.map { advMapper.toDTO( it ) }
    }

    // Return all hidden advertisements
    override fun getHiddenAdvertisements(): List<BannerDTO> {
        val adv =  bannerRepository.findHiddenAdvertisements()

        return adv.map { advMapper.toDTO( it ) }
    }

}
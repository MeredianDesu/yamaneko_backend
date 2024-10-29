package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.yamaneko.yamaneko_back_end.entity.Banner

// Repository for DB fetches
interface BannerRepository: JpaRepository<Banner, Long>{
    // Fetch advertisements WHERE visible == true
    @Query("SELECT a FROM Banner a WHERE a.visible = true")
    fun findVisibleAdvertisements(): List<Banner>

    // Fetch advertisements WHERE visible == false
    @Query("SELECT a FROM Banner a WHERE a.visible = false")
    fun findHiddenAdvertisements(): List<Banner>
}
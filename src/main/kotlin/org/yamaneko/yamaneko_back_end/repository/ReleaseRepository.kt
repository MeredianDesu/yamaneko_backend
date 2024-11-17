package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.Release

interface ReleaseRepository : JpaRepository<Release, Long>{
    @Query( value = "SELECT * FROM releases ORDER BY releases.uploaded_at DESC LIMIT :length", nativeQuery = true )
    fun findLastReleases( length: Int ): List<Release>

    @Query( "FROM Release r WHERE r.id = :releaseId" )
    fun findReleaseById( @Param("releaseId") releaseId: Long ): Release?

    @Query( "FROM Release r WHERE r.translatedName ILIKE :keyword" )
    fun findReleaseByName( @Param("keyword") keyword: String ): Release?

    @Query("FROM Release r JOIN r.genres g WHERE LOWER( g.name ) LIKE LOWER( :genre )" )
    fun findReleasesByGenre( @Param("genre") genre: String ): List<Release>?

}
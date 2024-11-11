package org.yamaneko.yamaneko_back_end.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yamaneko.yamaneko_back_end.entity.Release

interface ReleaseRepository : JpaRepository<Release, Long>{
    @Query( value = "SELECT * FROM releases ORDER BY releases.uploaded_at DESC LIMIT :length", nativeQuery = true )
    fun findLastReleases( length: Int ): List<Release>

    @Query("FROM Release r WHERE r.id = :releaseId")
    fun findReleaseById( @Param("releaseId") releaseId: Long ): Release?

//    @Query( "DELETE FROM Release r WHERE r.id = :releaseId" )
//    fun deleteById( @Param( "releaseId" ) releaseId: Long )
}
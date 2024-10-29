package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseDTO
import org.yamaneko.yamaneko_back_end.dto.release.ReleaseRequestPost
import org.yamaneko.yamaneko_back_end.service.release.ReleaseService

@RestController
@RequestMapping("/api/v1/releases" )
class ReleaseController( @Autowired private val releaseService: ReleaseService ) {

    @Operation( summary = "Get all releases from DB." )
    @GetMapping("")
    fun getReleases( @Parameter( description = "Number of releases received.", example = "4" ) @RequestParam( required = false ) length: Int? ): ResponseEntity< List< ReleaseDTO > > {
        val releases = if( length != null )
            releaseService.getLatestReleases( length )
        else
            releaseService.getAllReleases()

        return if( releases.isNotEmpty() )
            ResponseEntity.ok( releases )
        else
            ResponseEntity.status( HttpStatus.NO_CONTENT ).build()
    }

    @Operation( summary = "Get release by ID.")
    @GetMapping("{releaseId}")
    fun getReleaseById( @Parameter( description = "ID of release.", example = "4" ) @PathVariable( required = false ) releaseId: Long ): ResponseEntity<ReleaseDTO>{
        val release = releaseService.getRelease( releaseId )

        return if( release != null )
            ResponseEntity.ok ( release )
        else
            ResponseEntity.status( HttpStatus.NOT_FOUND ).build()
    }

    @Operation( summary = "Create new release." )
    @PostMapping("")
    fun saveRelease( @RequestBody request: ReleaseRequestPost ): ResponseEntity<ReleaseDTO>{
        val response = releaseService.createRelease( request )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }
}
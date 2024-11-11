package org.yamaneko.yamaneko_back_end.api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = ReleaseDTO::class ) ) ]
            ),
            ApiResponse( responseCode = "203",
                description = "No content",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "No releases added yet" ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
        ]
    )
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
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = ReleaseDTO::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Release not found" ) ) ]
            ),
        ]
    )
    fun getReleaseById( @Parameter( description = "ID of release.", example = "4" ) @PathVariable( required = false ) releaseId: Long ): ResponseEntity<ReleaseDTO>{
        val release = releaseService.getRelease( releaseId )

        return if( release != null )
            ResponseEntity.ok ( release )
        else
            ResponseEntity.status( HttpStatus.NOT_FOUND ).build()
    }

    @Operation( summary = "Create new release." )
    @PostMapping("")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Release created",
                content = [ Content( mediaType = "application/json", schema = Schema( implementation = ReleaseDTO::class ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
        ]
    )
    fun saveRelease( @RequestBody request: ReleaseRequestPost ): ResponseEntity<ReleaseDTO>{
        val response = releaseService.createRelease( request )

        return ResponseEntity.status( HttpStatus.CREATED ).body( response )
    }

    @Operation( summary = "Delete release" )
    @DeleteMapping("{releaseId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Release deleted",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Release deleted" ) ) ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Bad request" ) ) ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [ Content( mediaType = "text/plain", schema = Schema( type = "string", example = "Release not found" ) ) ]
            )
        ]
    )
    fun removeRelease( @Parameter( description = "Release ID" ) @PathVariable( required = true ) releaseId: Long ): ResponseEntity<String>{
        val status = releaseService.removeRelease( releaseId )

        return if( status.statusCode == HttpStatus.OK )
            ResponseEntity.status( HttpStatus.OK ).body( "Release deleted." )
        else
            ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Release not found." )
    }
}
package org.yamaneko.yamaneko_back_end.api.controllers.private_api.files_controllers

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("files" )
class FilesControllerV1 {

    @GetMapping( "/{filename:.+}" )
    fun getFile( @PathVariable filename: String ): ResponseEntity<Resource> {
        val filePath: Path = Paths.get( "/var/yamaneko_files" ).resolve( filename )
        val resource: Resource = UrlResource( filePath.toUri() )

        return if ( resource.exists() && resource.isReadable ) {
            val contentType = Files.probeContentType( filePath ) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE

            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType( contentType ) )
                .header( HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"" )
                .body( resource )
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
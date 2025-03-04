package org.yamaneko.yamaneko_back_end.api.controllers.private_api.files_controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.yamaneko.yamaneko_back_end.service.s3.S3Service
import java.nio.file.Files

@RestController
@RequestMapping("/api/files/v2")
class FilesControllerV2( private val s3Service: S3Service ) {

    @PostMapping("upload")
    fun uploadFile( @RequestParam( "file" ) file: MultipartFile ) {
        val tmpFile = Files.createTempFile( "upload-", file.originalFilename )
        file.transferTo( tmpFile )
        s3Service.uploadFile( tmpFile.toString(), file.originalFilename!! )
        Files.delete( tmpFile )
    }
}
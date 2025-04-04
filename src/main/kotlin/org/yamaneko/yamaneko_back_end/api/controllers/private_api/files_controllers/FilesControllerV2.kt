package org.yamaneko.yamaneko_back_end.api.controllers.private_api.files_controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.yamaneko.yamaneko_back_end.dto.s3.S3ResponseDTO
import org.yamaneko.yamaneko_back_end.service.s3.S3Service

@Tag(name = "{ V2 } Files API")
@RestController
@RequestMapping("/api/files/v2")
class FilesControllerV2(private val s3Service: S3Service) {
  
  @Operation(summary = "Get signed url for files uploading")
  @GetMapping("pre-signed-url")
  fun getPreSignedUrl(
    @RequestParam(value = "id", required = true) id: Long,
    @RequestParam(value = "fileName", required = true) fileName: String,
    @RequestParam(value = "type", required = true) type: String,
    @RequestParam(value = "isEpisode", required = true) isEpisode: Boolean,
  ): ResponseEntity<S3ResponseDTO> {
    
    return ResponseEntity.ok(s3Service.createUploadURL(id, fileName, type, isEpisode))
  }
}
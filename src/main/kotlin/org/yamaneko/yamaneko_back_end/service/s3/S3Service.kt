package org.yamaneko.yamaneko_back_end.service.s3

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Paths

@Service
class S3Service( private val s3Client: S3Client ) {
    private val bucketName = "yamanekospace"

    fun uploadFile( filePath:String, key: String ) {
        val file = Paths.get( filePath )
        val request = PutObjectRequest.builder()
            .bucket( bucketName )
            .key( key )
            .build()

        s3Client.putObject( request, RequestBody.fromFile( file ) )
        println("Файл загружен: $key")
    }
}
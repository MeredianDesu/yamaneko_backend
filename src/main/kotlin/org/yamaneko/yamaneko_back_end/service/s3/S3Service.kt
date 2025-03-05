package org.yamaneko.yamaneko_back_end.service.s3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.config.S3Config
import org.yamaneko.yamaneko_back_end.dto.s3.S3ResponseDTO
import org.yamaneko.yamaneko_back_end.md5
import software.amazon.awssdk.auth.credentials.*
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration

@Service
class S3Service {
    @Autowired
    lateinit var s3Config: S3Config

    // type: "release" | "image" | "banner"
    fun createUploadURL( id: Long, fileName: String, type: String ): S3ResponseDTO {
        val credentials = AwsBasicCredentials.create( s3Config.accessKey, s3Config.secretKey )

        S3Presigner.builder()
            .region(Region.of( s3Config.region ) )
            .credentialsProvider( StaticCredentialsProvider.create( credentials ) )
            .endpointOverride( URI.create( s3Config.endpoint ) )
            .build().use { presigner ->

                val putObjectRequest = PutObjectRequest.builder()
                    .bucket( s3Config.bucketName )
                    .key("$type/${id.toString().md5()}/$fileName")
                    .build()

                val presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes( 60 ))
                    .putObjectRequest(putObjectRequest)
                    .build()

                val presignedRequest = presigner.presignPutObject( presignRequest )

                val response = S3ResponseDTO(
                    uploadLink = presignedRequest.url().toString(),
                    link = "${s3Config.cdnEndpoint}/$type/${id.toString().md5()}/$fileName"
                )

                return response
            }
    }
}
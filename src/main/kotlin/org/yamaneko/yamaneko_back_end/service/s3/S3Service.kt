package org.yamaneko.yamaneko_back_end.service.s3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yamaneko.yamaneko_back_end.config.S3Config
import org.yamaneko.yamaneko_back_end.dto.s3.S3ResponseDTO
import org.yamaneko.yamaneko_back_end.md5
import org.yamaneko.yamaneko_back_end.service.episode.EpisodeService
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration

@Service
class S3Service {
  
  @Autowired
  lateinit var s3Config: S3Config
  
  @Autowired
  lateinit var episodeService: EpisodeService
  
  // type: "release" | "image" | "banner"
  // function for releases
  fun createUploadURL(id: Long, fileName: String, type: String, isEpisode: Boolean = false): S3ResponseDTO {
    val credentials = AwsBasicCredentials.create(s3Config.accessKey, s3Config.secretKey)
    
    val episodeNumber = episodeService.getEpisodesNumber(id) + 1
    
    S3Presigner.builder().region(Region.of(s3Config.region))
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .endpointOverride(URI.create(s3Config.endpoint)).build().use { presigner ->
        
        val putObjectRequest = when(isEpisode) {
          true -> {
            PutObjectRequest.builder().bucket(s3Config.bucketName)
              .key("$type/${id.toString().md5()}/episodes/$episodeNumber/$fileName").build()
          }
          
          false -> {
            PutObjectRequest.builder().bucket(s3Config.bucketName).key("$type/${id.toString().md5()}/$fileName").build()
          }
        }
        
        val presignRequest =
          PutObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(60)).putObjectRequest(putObjectRequest)
            .build()
        
        val presignedRequest = presigner.presignPutObject(presignRequest)
        
        val response = S3ResponseDTO(
          uploadLink = presignedRequest.url().toString(), link = when(isEpisode) {
            true -> {
              "${s3Config.cdnEndpoint}/$type/${id.toString().md5()}/episodes/$episodeNumber/$fileName"
            }
            
            false -> {
              "${s3Config.cdnEndpoint}/$type/${id.toString().md5()}/$fileName"
            }
          }
        )
        
        return response
      }
  }
  
  fun createUserPageLinks(username: String, filename: String): S3ResponseDTO {
    val credentials = AwsBasicCredentials.create(s3Config.accessKey, s3Config.secretKey)
    
    S3Presigner.builder().region(Region.of(s3Config.region))
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .endpointOverride(URI.create(s3Config.endpoint)).build().use { presigner ->
        val putObjectRequest = PutObjectRequest.builder().bucket(s3Config.bucketName).key("users/$username/$filename")
          .acl(ObjectCannedACL.PUBLIC_READ).build()
        
        val presignRequest =
          PutObjectPresignRequest.builder().signatureDuration(Duration.ofSeconds(10)).putObjectRequest(putObjectRequest)
            .build()
        
        val presignedRequest = presigner.presignPutObject(presignRequest)
        
        val response = S3ResponseDTO(
          uploadLink = presignedRequest.url().toString(), link = "${s3Config.cdnEndpoint}/users/$username/$filename"
        )
        
        return response
      }
  }
}
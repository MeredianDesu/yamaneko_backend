package org.yamaneko.yamaneko_back_end.config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3Config {
    @Value("\${cloud.access.key}") lateinit var accessKey: String
    @Value("\${cloud.secret.key}") lateinit var secretKey: String
    @Value("\${cloud.region}") lateinit var region: String
    @Value("\${cloud.bucketname}") lateinit var bucketName: String
    @Value("\${cloud.endpoint}") lateinit var endpoint: String
    @Value("\${cloud.cdn}") lateinit var cdnEndpoint: String

    @Bean
    fun s3Client(): S3Client {

        return S3Client.builder()
            .region( Region.of( region ) )
            .endpointOverride( URI.create( endpoint ) )
            .credentialsProvider( StaticCredentialsProvider.create( AwsBasicCredentials.create( accessKey,secretKey ) ) )
            .build()
    }
}
package org.yamaneko.yamaneko_back_end.config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3Config {
    @Bean
    fun s3Client(): S3Client {
        val accessKey = ""
        val secretKey = ""

        return S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("https://fra1.digitaloceanspaces.com"))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,secretKey)))
            .build()
    }
}
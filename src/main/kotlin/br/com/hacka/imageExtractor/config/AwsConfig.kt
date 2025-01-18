package br.com.hacka.imageExtractor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class AwsConfig (
  private val awsProperties: AwsProperties
) {

  private val region = Region.US_EAST_1
  @Bean
  fun s3Client(): S3Client =
    S3Client.builder()
      .region(region)
      .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
      .build()


  @Bean
  fun s3Presigner(): S3Presigner =
    S3Presigner.builder()
      .region(region)
      .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
      .build()

  @Bean
  fun sqsClient(): SqsClient =
    SqsClient.builder()
      .region(region)
      .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
      .build()

  @Bean
  fun dynamoDbClient(): DynamoDbClient {
    return DynamoDbClient.builder()
      .region(region)
      .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
      .build()
  }

  private fun getCredentials() =
    AwsSessionCredentials.create(
      awsProperties.accessKey,
      awsProperties.secretKey,
      awsProperties.sessionToken
    )
}
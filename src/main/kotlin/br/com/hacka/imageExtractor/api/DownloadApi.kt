package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.DynamoDbGateway
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.model.Message

@Service
class DownloadApi (
  private val sqsGateway: SqsGateway,
  private val dynamoDbGateway: DynamoDbGateway,
  private val s3Gateway: S3Gateway,
  private val storageController: StorageController
) {

  @Scheduled(fixedRate = 10000)
  fun getDownloadMessage() {
      storageController.getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)
  }

}
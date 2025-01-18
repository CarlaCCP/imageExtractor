package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class DownloadApi (
  private val sqsGateway: SqsGateway,
  private val storageGateway: StorageGateway,
  private val s3Gateway: S3Gateway,
  private val storageController: StorageController
) {

  @Scheduled(fixedRate = 10000)
  fun getDownloadMessage() {
      storageController.getPresignDownloadUrl(sqsGateway, storageGateway, s3Gateway)
  }

}
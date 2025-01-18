package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.StorageConfig
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class StorageController (

  private val storageConfig: StorageConfig
) {

  fun upload (
    storageGateway: IStorageGateway,
    sqsGateway: SqsGateway,
    dynamoDbGateway: StorageGateway,
    file: MultipartFile
  ) : Storage {
    return storageConfig.storageUseCase().uploadFile(storageGateway, sqsGateway, dynamoDbGateway, file)
  }

  fun download(storageGateway: StorageGateway, id: String) : Storage {
    return storageConfig.storageUseCase().download(storageGateway, id)
  }

  fun getPresignDownloadUrl(
    sqsGateway: SqsGateway,
    storageGateway: StorageGateway,
    s3Gateway: S3Gateway
  ) {
    storageConfig.storageUseCase().getPresignDownloadUrl(sqsGateway, storageGateway, s3Gateway)
  }
}
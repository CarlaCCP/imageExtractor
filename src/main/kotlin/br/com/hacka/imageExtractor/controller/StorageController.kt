package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.StorageConfig
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.DynamoDbGateway
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
    dynamoDbGateway: DynamoDbGateway,
    file: MultipartFile
  ) : Storage {
    return storageConfig.storageUseCase().uploadFile(storageGateway, sqsGateway, dynamoDbGateway, file)
  }

  fun download(dynamoDbGateway: DynamoDbGateway, id: String) : Storage {
    return storageConfig.storageUseCase().download(dynamoDbGateway, id)
  }

  fun getPresignDownloadUrl(
    sqsGateway: SqsGateway,
    dynamoDbGateway: DynamoDbGateway,
    s3Gateway: S3Gateway
  ) {
    storageConfig.storageUseCase().getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)
  }
}
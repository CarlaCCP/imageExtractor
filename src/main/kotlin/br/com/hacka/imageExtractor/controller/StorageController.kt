package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.StorageConfig
import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import org.springframework.stereotype.Component

@Component
class StorageController (

  private val storageConfig: StorageConfig
) {

  fun download(storageGateway: StorageGateway, id: String) : Storage {
    return storageConfig.storageUseCase().download(storageGateway, id)
  }

  fun extractor (
    sqsGateway: SqsGateway,
    dynamoDbGateway: StorageGateway,
   updateRequest: UploadRequest
  ) = storageConfig.storageUseCase().upload(sqsGateway, dynamoDbGateway, updateRequest)

  fun getPresignDownloadUrl(
    sqsGateway: SqsGateway,
    storageGateway: StorageGateway,
    s3Gateway: S3Gateway
  ) {
    storageConfig.storageUseCase().getPresignDownloadUrl(sqsGateway, storageGateway, s3Gateway)
  }

  fun getUploadUrl(storageGateway: IStorageGateway, dynamoDbGateway: StorageGateway, filename: String) : UploadRequest{
    return storageConfig.storageUseCase().uploadUrl(storageGateway, dynamoDbGateway, filename)
  }

  fun getDownloads (storageGateway: StorageGateway, user: String) : List<Storage> {
    return storageConfig.storageUseCase().getDownloads(storageGateway, user)
  }
}
package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.StorageConfig
import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*

class StorageControllerTest : FunSpec ({

  lateinit var storageConfig: StorageConfig
  lateinit var storageGateway: IStorageGateway
  lateinit var dynamoDbGateway: StorageGateway
  lateinit var sqsGateway: SqsGateway
  lateinit var s3Gateway: S3Gateway

  lateinit var storageController: StorageController

  beforeTest {
    storageConfig = mockk<StorageConfig>()
    storageGateway = mockk<IStorageGateway>()
    dynamoDbGateway = mockk<StorageGateway>()
    sqsGateway = mockk<SqsGateway>()
    s3Gateway = mockk<S3Gateway>()

    storageController = StorageController(
      storageConfig
    )
  }

  test("Should call download") {
    every { storageConfig.storageUseCase().download(dynamoDbGateway, "123") } returns Storage()

    shouldNotBeNull {
      storageController.download(dynamoDbGateway, "123")
    }
  }


  test("Should call extractor") {
    every { storageConfig.storageUseCase().upload(sqsGateway, dynamoDbGateway, UploadRequest()) } returns Storage()

    shouldNotBeNull {
      storageController.extractor(sqsGateway, dynamoDbGateway, UploadRequest())
    }
  }


  test("Should get presign download url") {
    every { storageConfig.storageUseCase().getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway) } just runs

    storageController.getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)

    verify {
      storageController.getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)
    }
  }

  test("Should get upload url") {
    every {
      storageConfig.storageUseCase().uploadUrl(storageGateway, dynamoDbGateway, "show.mp4")
    } returns UploadRequest()


    shouldNotBeNull {
      storageController.getUploadUrl(storageGateway, dynamoDbGateway, "show.mp4")
    }
  }

  test("Should get downloads by user") {
    every {
      storageConfig.storageUseCase().getDownloads(dynamoDbGateway, "carla@teste.com")
    } returns listOf(Storage())

    shouldNotBeNull {
      storageController.getDownloads(dynamoDbGateway, "carla@teste.com")
    }
  }

})
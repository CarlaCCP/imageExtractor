package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk

class StorageApiTest : FunSpec ({

  lateinit var storageGateway: IStorageGateway
  lateinit var storageController: StorageController
  lateinit var dynamoDbGateway: StorageGateway
  lateinit var sqsGateway: SqsGateway
  lateinit var storageApi: StorageApi

  beforeTest {

    storageGateway = mockk<IStorageGateway>()
    storageController = mockk<StorageController>()
    dynamoDbGateway = mockk<StorageGateway>()
    sqsGateway = mockk<SqsGateway>()

    storageApi = StorageApi ( storageGateway, storageController, dynamoDbGateway, sqsGateway )
  }

  test("Should get download by id") {
    every { storageController.download(dynamoDbGateway, "123") } returns Storage()

    shouldNotBeNull {
      storageApi.download("123")
    }
  }

  test("Should get downloads by user") {
    every {
      storageController.getDownloads(dynamoDbGateway, "carla@teste.com")
    } returns listOf(Storage())

    shouldNotBeNull {
      storageApi.getDownloads("carla@teste.com")
    }
  }

  test("Should get upload url") {
    every {
      storageController.getUploadUrl(storageGateway, dynamoDbGateway, "show.mp4")
    } returns UploadRequest()

    shouldNotBeNull {
      storageApi.uploadUrl("show.mp4")
    }
  }

  test("Should start extractor") {
    every {
      storageController.extractor(sqsGateway, dynamoDbGateway, UploadRequest())
    } returns Storage()

    shouldNotBeNull {
      storageApi.extractor(UploadRequest())
    }
  }
})
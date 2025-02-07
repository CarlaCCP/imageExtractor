package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.gateway.StorageGateway
import io.kotest.core.spec.style.FunSpec
import io.mockk.*

class DownloadApiTest : FunSpec({

  lateinit var sqsGateway: SqsGateway
  lateinit var storageGateway: StorageGateway
  lateinit var s3Gateway: S3Gateway
  lateinit var storageController: StorageController
  lateinit var downloadApi: DownloadApi


  beforeTest {
    sqsGateway = mockk<SqsGateway>()
    storageGateway = mockk<StorageGateway>()
    s3Gateway = mockk<S3Gateway>()
    storageController = mockk<StorageController>()

    downloadApi = DownloadApi(
      sqsGateway, storageGateway, s3Gateway, storageController
    )
  }

  test("Should get download message") {
    every { storageController.getPresignDownloadUrl(sqsGateway, storageGateway, s3Gateway) } just runs

    downloadApi.getDownloadMessage()

    verify { downloadApi.getDownloadMessage() }
  }

})
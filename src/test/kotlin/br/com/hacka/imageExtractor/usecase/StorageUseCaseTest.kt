package br.com.hacka.imageExtractor.usecase

import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.core.event.UpdateEvent
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import software.amazon.awssdk.services.sqs.model.Message

class StorageUseCaseTest : FunSpec ({

  lateinit var sqsGateway: SqsGateway
  lateinit var dynamoDbGateway: StorageGateway
  lateinit var s3Gateway: S3Gateway
  lateinit var storageGateway: IStorageGateway

  lateinit var storageUseCase: StorageUseCase

  beforeTest {
    sqsGateway = mockk<SqsGateway>()
    dynamoDbGateway = mockk<StorageGateway>()
    s3Gateway = mockk<S3Gateway>()
    storageGateway = mockk<IStorageGateway>()

    storageUseCase = StorageUseCase()
  }

  test("Should upload file") {

    every { dynamoDbGateway.getItem("123") } returns Storage(userEmail = "teste@teste.com")
    every { dynamoDbGateway.updateUserEmail(any<Storage>()) } returns Storage()
    every { sqsGateway.sendMessage(any<Storage>()) } just runs

    shouldNotBeNull {
      storageUseCase.upload(sqsGateway, dynamoDbGateway, UploadRequest(id = "123"))
    }
  }

  test("Should download") {

  every { dynamoDbGateway.getItem(any<String>()) } returns Storage()

    shouldNotBeNull {
      storageUseCase.download( dynamoDbGateway, "123")
    }
  }

  test("Should get downloads") {

    every { dynamoDbGateway.getItemByUser(any<String>()) } returns listOf(Storage())

    shouldNotBeNull {
      storageUseCase.getDownloads( dynamoDbGateway, "123")
    }
  }

  test("Should get presign download url") {
    val updateEvent = UpdateEvent("1232", "123/show.mp4", "")

    every {
      sqsGateway.getMessage()
    } returns listOf(Message.builder().body(jacksonObjectMapper().writeValueAsString(updateEvent)).build())
    every { dynamoDbGateway.getItem(any<String>()) } returns Storage()
    every { dynamoDbGateway.update(any<Storage>()) } returns Storage()
    every { sqsGateway.deleteMessage(any<Message>()) } just runs

    shouldNotBeNull {
      storageUseCase.getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)
    }
  }


  test("Should not get presign download url") {
    val updateEvent = UpdateEvent("1232", "123/show.mp4", "Erro no download")

    every {
      sqsGateway.getMessage()
    } returns listOf(Message.builder().body(jacksonObjectMapper().writeValueAsString(updateEvent)).build())
    every { dynamoDbGateway.getItem(any<String>()) } returns Storage()
    every { dynamoDbGateway.update(any<Storage>()) } returns Storage()
    every { sqsGateway.deleteMessage(any<Message>()) } just runs

    shouldNotBeNull {
      storageUseCase.getPresignDownloadUrl(sqsGateway, dynamoDbGateway, s3Gateway)
    }
  }


  test("Should get upload url") {

    every { storageGateway.getUploadPresignUrl(any<String>()) } returns "url"
    every { dynamoDbGateway.save(any<Storage>()) } just runs

    shouldNotBeNull {
      storageUseCase.uploadUrl(storageGateway, dynamoDbGateway, "13/show.mp4")
    }
  }
})
package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Storage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.*

class SqsGatewayTest : FunSpec({

  lateinit var sqsClient: SqsClient

  lateinit var sqsGateway: SqsGateway


  beforeTest {
    sqsClient = mockk<SqsClient>()

    sqsGateway = SqsGateway(sqsClient)
  }

  test("Should send message") {
    val response = SendMessageResponse.builder().build()
    every { sqsClient.sendMessage(any<SendMessageRequest>()) } returns response

    shouldNotBeNull {
      sqsGateway.sendMessage(Storage())
    }
  }

  test("Should get message") {
    val response = ReceiveMessageResponse.builder().build()
    every { sqsClient.receiveMessage(any<ReceiveMessageRequest>()) } returns response

    shouldNotBeNull {
      sqsGateway.getMessage()
    }
  }

  test("Should delete message") {
    val response = DeleteMessageResponse.builder().build()
    every { sqsClient.deleteMessage(any<DeleteMessageRequest>()) } returns response

    shouldNotBeNull {
      sqsGateway.deleteMessage(Message.builder().build())
    }
  }

})
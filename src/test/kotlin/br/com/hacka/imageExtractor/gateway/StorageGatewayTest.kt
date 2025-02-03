package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Storage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

class StorageGatewayTest : FunSpec({

  lateinit var dynamoDbClient: DynamoDbClient

  lateinit var storageGateway: StorageGateway

  beforeTest {
    dynamoDbClient = mockk<DynamoDbClient>()

    storageGateway = StorageGateway(dynamoDbClient)
  }

  test("Should save storage") {
    every { dynamoDbClient.putItem(any<PutItemRequest>()) } returns PutItemResponse.builder().build()

    shouldNotBeNull {
      storageGateway.save(Storage())
    }
  }

  test("Should update storage") {
    every { dynamoDbClient.updateItem(any<UpdateItemRequest>()) } returns UpdateItemResponse.builder().build()

    shouldNotBeNull {
      storageGateway.update(Storage())
    }
  }

  test("Should update user email") {
    every { dynamoDbClient.updateItem(any<UpdateItemRequest>()) } returns UpdateItemResponse.builder().build()

    shouldNotBeNull {
      storageGateway.updateUserEmail(Storage())
    }
  }

  test("Should get item") {
    val response = GetItemResponse
      .builder()
      .item(
        mapOf(
          "id" to AttributeValue.builder().s("123").build(),
          "downloadStatus" to AttributeValue.builder().s("downloadStatus").build(),
          "downloadUrl" to AttributeValue.builder().s("downloadUrl").build(),
          "uploadFilename" to AttributeValue.builder().s("uploadFilename").build(),
          "downloadFilename" to AttributeValue.builder().s("downloadFilename").build(),
          "userEmail" to AttributeValue.builder().s("userEmail").build(),
        )
      )
      .build()

    every { dynamoDbClient.getItem(any<GetItemRequest>()) } returns response

    shouldNotBeNull {
      storageGateway.getItem("122")
    }
  }

  test("Should get item by user") {
    val response = QueryResponse
      .builder()
      .items(
        mapOf(
          "id" to AttributeValue.builder().s("123").build(),
          "downloadStatus" to AttributeValue.builder().s("downloadStatus").build(),
          "downloadUrl" to AttributeValue.builder().s("downloadUrl").build(),
          "uploadFilename" to AttributeValue.builder().s("uploadFilename").build(),
          "downloadFilename" to AttributeValue.builder().s("downloadFilename").build(),
          "userEmail" to AttributeValue.builder().s("userEmail").build(),
        )
      )
      .build()

    every { dynamoDbClient.query(any<QueryRequest>()) } returns response

    shouldNotBeNull {
      storageGateway.getItemByUser("teste@teste.com")
    }
  }

})
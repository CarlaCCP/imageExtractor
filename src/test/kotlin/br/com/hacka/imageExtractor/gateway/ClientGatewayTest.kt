package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Client
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

class ClientGatewayTest : FunSpec({

  lateinit var dynamoDbClient: DynamoDbClient

  lateinit var clientGateway: ClientGateway

  beforeTest {
    dynamoDbClient = mockk<DynamoDbClient>()
    clientGateway = ClientGateway(dynamoDbClient)
  }

  val client = Client(
    "4697828546525",
    "Maria",
    "maria@teste.com",
    "****"
  )

  test("Should save client") {
    val request = PutItemRequest.builder()
      .tableName("cliente")
      .item(
        mapOf(
          "cpf" to AttributeValue.builder().s(client.cpf).build(),
          "nome" to AttributeValue.builder().s(client.nome).build(),
          "email" to AttributeValue.builder().s(client.email).build(),
          "senha" to AttributeValue.builder().s(client.senha).build()
        )
      )
      .build()

    every { dynamoDbClient.putItem(request) } returns PutItemResponse.builder().build()

    shouldNotBeNull {
      clientGateway.save(client)
    }
  }

  test("Should find by id") {
    val request = GetItemRequest.builder()
      .tableName("cliente")
      .key(
        mapOf(
          "cpf" to AttributeValue.builder().s("1213131").build()
        )
      )
      .build()

    every { dynamoDbClient.getItem(request) } returns GetItemResponse.builder().build()

    shouldNotBeNull {
      clientGateway.findByCpf("1213131")
    }
  }

})
package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Client
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

@Repository
class ClientGateway(
  private val dynamoDbClient: DynamoDbClient
) {
  private val tableName = "cliente"

  fun save(client: Client): Client {
    val request = PutItemRequest.builder()
      .tableName(tableName)
      .item(
        mapOf(
          "cpf" to AttributeValue.builder().s(client.cpf).build(),
          "nome" to AttributeValue.builder().s(client.nome).build(),
          "email" to AttributeValue.builder().s(client.email).build(),
          "senha" to AttributeValue.builder().s(client.senha).build()
        )
      )
      .build()
    dynamoDbClient.putItem(request)
    return client
  }

  fun findByCpf(cpf: String): Client {
    val request = GetItemRequest.builder()
      .tableName(tableName)
      .key(
        mapOf(
          "cpf" to AttributeValue.builder().s(cpf).build()
        )
      )
      .build()
    val response = dynamoDbClient.getItem(request)
    return Client (
      cpf = response.item()["cpf"]?.s(),
      nome = response.item()["nome"]?.s(),
      email = response.item()["email"]?.s(),
    )

  }

}
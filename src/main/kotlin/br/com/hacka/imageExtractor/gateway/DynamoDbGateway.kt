package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Storage
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import java.util.UUID

@Repository
class DynamoDbGateway (

  private val dynamoDbClient: DynamoDbClient
) {

  private val tableName = "extractor"

  fun save (storage: Storage) {
    val request = PutItemRequest.builder()
      .tableName(tableName)
      .item(
        mapOf(
          "id" to AttributeValue.builder().s(storage.id).build(),
          "uploadFilename" to AttributeValue.builder().s(storage.uploadFilename).build(),
          "downloadStatus" to AttributeValue.builder().s(storage.downloadStatus).build(),
          "ttl" to  AttributeValue.builder().s(storage.ttl?.toString()).build()
        )
      )
      .build()
    dynamoDbClient.putItem(request)
  }

  fun update (storage: Storage): Storage {
    val updateItemRequest = UpdateItemRequest.builder()
      .tableName(tableName)
      .key(
        mapOf(
          "id" to AttributeValue.builder().s(storage.id).build()
        )
      )
      .updateExpression("SET downloadFilename = :downloadFilename, downloadUrl = :downloadUrl, downloadStatus = :downloadStatus")
      .expressionAttributeValues(
        mapOf(
          ":downloadFilename" to AttributeValue.builder().s(storage.downloadFilename).build(),
          ":downloadUrl" to AttributeValue.builder().s(storage.downloadUrl).build(),
          ":downloadStatus" to AttributeValue.builder().s(storage.downloadStatus).build()
        )
      )
      .build()

    dynamoDbClient.updateItem(updateItemRequest)
    return storage
  }


  fun getItem(id: String) : Storage {
    val request = GetItemRequest.builder()
      .tableName(tableName)
      .key(
        mapOf(
          "id" to AttributeValue.builder().s(id).build()
        )
      )
      .build()
    val response = dynamoDbClient.getItem(request)
    return Storage(
      id = response.item()["id"]?.s(),
      downloadStatus = response.item()["downloadStatus"]?.s(),
      downloadUrl = response.item()["downloadUrl"]?.s(),
      uploadFilename = response.item()["uploadFilename"]?.s(),
      downloadFilename = response.item()["downloadFilename"]?.s()
    )
  }

}
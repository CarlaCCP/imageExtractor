package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.core.entity.Storage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Repository
class SqsGateway (
  private val sqsClient: SqsClient
){

  private val log = KotlinLogging.logger {}

  private val createQueue = "https://sqs.us-east-1.amazonaws.com/118934669452/create-frames-queue"
  private val updateQueue = "https://sqs.us-east-1.amazonaws.com/118934669452/update-frames-queue"

  fun sendMessage(storage: Storage) {
    val sendMessage = SendMessageRequest.builder()
      .queueUrl(createQueue)
      .messageBody(jacksonObjectMapper().writeValueAsString(storage))
      .build()

    sqsClient.sendMessage(sendMessage)
  }

  fun getMessage(): List<Message>? {
    val receiveMessageRequest = ReceiveMessageRequest.builder()
      .queueUrl(updateQueue)
      .maxNumberOfMessages(1)
      .waitTimeSeconds(10)
      .build()

    return sqsClient.receiveMessage(receiveMessageRequest).messages()
  }

  fun deleteMessage(message: Message) {
      val deleteRequest = DeleteMessageRequest.builder()
        .queueUrl(updateQueue)
        .receiptHandle(message.receiptHandle())
        .build()

    sqsClient.deleteMessage(deleteRequest)
    log.info { "Mensagem excluida com sucesso" }
  }


}
package br.com.hacka.imageExtractor.usecase

import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.core.entity.Storage
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.S3Gateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.joda.time.Instant
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.UUID

class StorageUseCase {
  private val log = KotlinLogging.logger {}

  fun uploadFile(
    storageGateway: IStorageGateway,
    sqsGateway: SqsGateway,
    dynamoDbGateway: StorageGateway,
    file: MultipartFile
  ): Storage {

    val id = UUID.randomUUID().toString()

    val tempFile = Files.createTempFile(file.originalFilename, null)
    file.inputStream.use { inputStream ->
      Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING)
    }

    val filename = storageGateway.upload(file, tempFile, id)

    Files.delete(tempFile)

    val storage = Storage(
      id = id,
      uploadFilename = filename,
      downloadStatus = "processando",
      ttl = Instant.now().plus(3).millis
    )
    sqsGateway.sendMessage(storage)
    dynamoDbGateway.save(storage)
    return storage
  }


  fun upload (
    sqsGateway: SqsGateway,
    dynamoDbGateway: StorageGateway,
    id: String ) : Storage
  {
   val storage = dynamoDbGateway.getItem(id)
    sqsGateway.sendMessage(storage)
    log.info { "Arquivo enviado para extração" }
    return storage
  }

  fun download (storageGateway: StorageGateway, id: String) : Storage {
    return storageGateway.getItem(id)
  }


  fun getPresignDownloadUrl(
    sqsGateway: SqsGateway,
    storageGateway: StorageGateway,
    s3Gateway: S3Gateway
  ) {
    val message = sqsGateway.getMessage()
    if (message.isNullOrEmpty()) {
      log.info { "Fila vazia" }
    } else {
      log.info { "Processo de download iniciado" }
      message.map {
        val storage = convertToObject(it.body())
        val downloadUrl = s3Gateway.getPresignUrl(storage.downloadFilename.toString())
        storageGateway.update(
          storage.copy(
            downloadUrl = downloadUrl,
            downloadStatus = "download completo"
          )
        )
        sqsGateway.deleteMessage(it)
      }
    }
  }

  fun uploadUrl (storageGateway: IStorageGateway, dynamoDbGateway: StorageGateway, filename: String): UploadRequest {
    val path = UUID.randomUUID().toString()
    val file = "$path/$filename"
    val url = storageGateway.getUploadPresignUrl(file)


    dynamoDbGateway.save(
      Storage(
      id = path, uploadFilename = filename, downloadStatus = "processando", ttl = Instant.now().plus(3).millis
    ))

    return UploadRequest(fileName = file, url = url, id = path)
  }


  private fun convertToObject(message: String): Storage {
    return jacksonObjectMapper().readValue<Storage>(message)
  }

}
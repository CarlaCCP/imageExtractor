package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.core.dto.UploadRequest
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/extract")
@Validated
class StorageApi (
  private val storageGateway: IStorageGateway,
  private val storageController: StorageController,
  private val dynamoDbGateway: StorageGateway,
  private val sqsGateway: SqsGateway
) {

  @PostMapping("/storage")
  fun storage(
    @RequestPart("file") videoFile: MultipartFile
  ) = storageController.upload(storageGateway, sqsGateway, dynamoDbGateway, videoFile)

  @PostMapping
  fun extractor(@RequestBody uploadRequest: UploadRequest) =
    storageController.extractor(sqsGateway, dynamoDbGateway, uploadRequest)
  @GetMapping("/download/{id}")
  fun download(@PathVariable id: String) = storageController.download(dynamoDbGateway, id)

  @GetMapping("upload/{filename}")
  fun uploadUrl(@PathVariable filename: String) = storageController.getUploadUrl(storageGateway, dynamoDbGateway, filename)

  @GetMapping
  fun getDownloads (@Valid user: String) = storageController.getDownloads(dynamoDbGateway, user)
}
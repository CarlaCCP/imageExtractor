package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.StorageController
import br.com.hacka.imageExtractor.gateway.StorageGateway
import br.com.hacka.imageExtractor.gateway.SqsGateway
import br.com.hacka.imageExtractor.interfaces.IStorageGateway
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

  @GetMapping("/download/{id}")
  fun download(@PathVariable id: String) = storageController.download(dynamoDbGateway, id)

}
package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.sqs.SqsClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.Duration

@Repository
class S3Gateway (
  private val s3Client: S3Client,
  private val s3Presigner: S3Presigner,
): IStorageGateway {

  private val bucketName = "storage-image-extractor"

  override fun upload(file: MultipartFile, path: Path, id: String) : String {

    val putObjectRequest = PutObjectRequest.builder()
      .bucket(bucketName)
      .key("$id/${file.originalFilename}")
      .build()

    s3Client.putObject(putObjectRequest, path)

    return file.originalFilename.toString()
  }


  override fun getPresignUrl(fileKey: String): String {
    val getObjectRequest = GetObjectRequest
      .builder()
      .bucket(bucketName)
      .key(fileKey)
      .build()

    val getObjectPresignObjectRequest = GetObjectPresignRequest
      .builder()
      .signatureDuration(Duration.ofDays(1))
      .getObjectRequest(getObjectRequest)
      .build()

    return s3Presigner.presignGetObject(getObjectPresignObjectRequest).url().toString()
  }

}
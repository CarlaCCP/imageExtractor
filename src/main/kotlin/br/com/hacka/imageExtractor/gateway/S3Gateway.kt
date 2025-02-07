package br.com.hacka.imageExtractor.gateway

import br.com.hacka.imageExtractor.interfaces.IStorageGateway
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Repository
class S3Gateway (
  private val s3Presigner: S3Presigner,
): IStorageGateway {

  private val bucketName = "storage-image-extractor"


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

  override fun getUploadPresignUrl(fileName: String): String {
    val putObjectRequest = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(fileName)
      .build()

    val url = PutObjectPresignRequest.builder()
      .signatureDuration(Duration.ofMinutes(10))
      .putObjectRequest(putObjectRequest)
      .build()

    return s3Presigner.presignPutObject(url).url().toString()

  }

}
package br.com.hacka.imageExtractor.gateway

import io.kotest.assertions.any
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.*
import software.amazon.awssdk.http.SdkHttpMethod
import software.amazon.awssdk.http.SdkHttpRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Instant

class S3GatewayTest : FunSpec({

  lateinit var s3Presigner: S3Presigner

  lateinit var s3Gateway: S3Gateway

  beforeTest {
    s3Presigner = mockk<S3Presigner>()

    s3Gateway = S3Gateway(s3Presigner)
  }

  test("Should get presign url") {

    val response = PresignedGetObjectRequest
      .builder()
      .expiration(Instant.now())
      .signedHeaders(mapOf("teste" to listOf("teste")))
      .isBrowserExecutable(true)
      .httpRequest(
        SdkHttpRequest
          .builder()
          .protocol("http")
          .host("http://localhost:8080.com")
          .method(SdkHttpMethod.PUT)
          .build()
      )
      .build()


    every { s3Presigner.presignGetObject(any<GetObjectPresignRequest>()) } returns response


    shouldNotBeNull {
      s3Gateway.getPresignUrl("123/show.mp4")
    }
  }


  test("Should get upload url") {

    val response = PresignedPutObjectRequest
      .builder()
      .expiration(Instant.now())
      .signedHeaders(mapOf("teste" to listOf("teste")))
      .isBrowserExecutable(true)
      .httpRequest(
        SdkHttpRequest
          .builder()
          .protocol("http")
          .host("http://localhost:8080.com")
          .method(SdkHttpMethod.PUT)
          .build()
      )
      .build()


    every { s3Presigner.presignPutObject(any<PutObjectPresignRequest>()) } returns response


    shouldNotBeNull {
      s3Gateway.getUploadPresignUrl("123/show.mp4")
    }
  }
})
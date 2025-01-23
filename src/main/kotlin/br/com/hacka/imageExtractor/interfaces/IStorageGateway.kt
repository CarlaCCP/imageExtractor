package br.com.hacka.imageExtractor.interfaces


import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

interface IStorageGateway {

  fun upload (file: MultipartFile, path: Path, id: String): String

  fun getPresignUrl (fileKey: String): String

  fun getUploadPresignUrl (fileName: String): String
}
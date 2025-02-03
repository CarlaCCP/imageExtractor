package br.com.hacka.imageExtractor.interfaces


interface IStorageGateway {

  fun getPresignUrl (fileKey: String): String

  fun getUploadPresignUrl (fileName: String): String
}
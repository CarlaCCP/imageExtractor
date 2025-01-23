package br.com.hacka.imageExtractor.core.dto

data class UploadRequest(
  val id: String? = null,
  val fileName: String? = null,
  val url: String? = null
)

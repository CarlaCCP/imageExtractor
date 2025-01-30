package br.com.hacka.imageExtractor.core.event

data class UpdateEvent(
  val id: String? = null,
  val downloadFilename: String? = null,
  val error: String? = null
)

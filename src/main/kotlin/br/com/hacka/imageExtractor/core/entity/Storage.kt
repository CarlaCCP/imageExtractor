package br.com.hacka.imageExtractor.core.entity

import org.joda.time.Instant

data class Storage (
  val id: String? = null,
  val uploadFilename: String? = null,
  val downloadFilename: String? = null,
  val downloadStatus: String? = null,
  val downloadUrl: String? = null,
  val ttl: Long? = null,
)
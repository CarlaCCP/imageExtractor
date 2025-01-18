package br.com.hacka.imageExtractor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io
import org.springframework.boot.runApplication
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File
import java.io.IOException

@SpringBootApplication
@EnableScheduling
class ImageExtractorApplication

fun main(args: Array<String>) {
  runApplication<ImageExtractorApplication>(*args)
}

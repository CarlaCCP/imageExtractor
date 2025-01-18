package br.com.hacka.imageExtractor.config

import br.com.hacka.imageExtractor.usecase.StorageUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig {

  @Bean
  fun storageUseCase() : StorageUseCase {
    return StorageUseCase()
  }
}
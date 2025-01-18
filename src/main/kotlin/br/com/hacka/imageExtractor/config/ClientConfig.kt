package br.com.hacka.imageExtractor.config

import br.com.hacka.imageExtractor.usecase.ClientUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientConfig {

  @Bean
  fun clientUseCase() : ClientUseCase {
    return ClientUseCase()
  }

}
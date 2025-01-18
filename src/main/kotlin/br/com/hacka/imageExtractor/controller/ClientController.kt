package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.ClientConfig
import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway
import org.springframework.stereotype.Component

@Component
class ClientController (
  private val  clientConfig: ClientConfig
){

  fun save(clientGateway: ClientGateway, client: Client) : Client {
    return clientConfig.clientUseCase().save(clientGateway, client)
  }

  fun getClient(clientGateway: ClientGateway, cpf: String) : Client {
    return clientConfig.clientUseCase().getClient(clientGateway, cpf)
  }
}
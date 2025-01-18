package br.com.hacka.imageExtractor.usecase

import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway

class ClientUseCase {

  fun save (clientGateway: ClientGateway, client: Client) : Client {
    return clientGateway.save(client)
  }

  fun getClient (clientGateway: ClientGateway, cpf: String) : Client {
    return clientGateway.findByCpf(cpf)
  }
}
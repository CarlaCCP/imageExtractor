package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.ClientController
import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/client")
@Validated
class ClientApi (
  private val clientGateway: ClientGateway,
  private val clientController: ClientController
){

  @PostMapping
  fun save (@RequestBody client: Client) : Client =
    clientController.save(clientGateway, client)

  @GetMapping("/{cpf}")
  fun getClient(@PathVariable cpf: String) : Client =
    clientController.getClient(clientGateway, cpf)

}
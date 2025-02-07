package br.com.hacka.imageExtractor.controller

import br.com.hacka.imageExtractor.config.ClientConfig
import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk

class ClientControllerTest : FunSpec({

  lateinit var clientConfig: ClientConfig
  lateinit var clientGateway: ClientGateway
  lateinit var clientController: ClientController

  beforeTest {
    clientConfig = mockk<ClientConfig>()
    clientGateway = mockk<ClientGateway>()

    clientController = ClientController(clientConfig)
  }

  val client =  Client (
    "4697828546525",
    "Maria",
    "maria@teste.com",
    "****"
  )


  test("Should save client") {
    every { clientConfig.clientUseCase().save(clientGateway, client) } returns client

    shouldNotBeNull {
      clientController.save(clientGateway, client)
    }
  }

  test("Should get client by cpf") {
    every { clientConfig.clientUseCase().getClient(clientGateway, "454545445") } returns client

    shouldNotBeNull {
      clientController.getClient(clientGateway, "454545445")
    }
  }
})
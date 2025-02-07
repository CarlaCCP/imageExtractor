package br.com.hacka.imageExtractor.api

import br.com.hacka.imageExtractor.controller.ClientController
import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ClientApiTest : FunSpec({

  lateinit var clientGateway: ClientGateway
  lateinit var clientController: ClientController
  lateinit var clientApi: ClientApi

  beforeTest {
    clientGateway = mockk<ClientGateway>()
    clientController = mockk<ClientController>()
    clientApi = ClientApi(clientGateway, clientController)
  }

  val client =  Client (
    "4697828546525",
    "Maria",
    "maria@teste.com",
    "****"
  )
  test("Should save client with success") {
    every { clientController.save(clientGateway, client) } returns client

    clientApi.save(client).cpf shouldBe client.cpf

  }

  test("Should get client by cpf") {
    every { clientController.getClient(clientGateway, client.cpf.toString()) } returns client

    shouldNotBeNull {
      clientApi.getClient(client.cpf.toString())
    }

  }
})
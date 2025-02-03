package br.com.hacka.imageExtractor.usecase

import br.com.hacka.imageExtractor.core.entity.Client
import br.com.hacka.imageExtractor.gateway.ClientGateway
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk

class ClientUseCaseTest : FunSpec({

  lateinit var clientGateway: ClientGateway
  lateinit var clientUseCase: ClientUseCase


  beforeTest {
    clientGateway = mockk<ClientGateway>()

    clientUseCase = ClientUseCase()
  }
  val client = Client(
    "4697828546525",
    "Maria",
    "maria@teste.com",
    "****"
  )

  test("Should save client") {
    every { clientGateway.save(client) } returns client
    shouldNotBeNull {
      clientUseCase.save(clientGateway, client)
    }
  }

  test("Should get client") {
    every { clientGateway.findByCpf(client.cpf.toString()) } returns client
    shouldNotBeNull {
      clientUseCase.getClient(clientGateway, client.cpf.toString())
    }
  }
})
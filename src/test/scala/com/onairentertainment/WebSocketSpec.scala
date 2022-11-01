package com.onairentertainment

import akka.http.scaladsl.testkit.{ScalatestRouteTest, WSProbe}
import com.onairentertainment.controllers.GameController
import com.onairentertainment.models.Player
import com.onairentertainment.models.ping.pong.Pong
import com.onairentertainment.models.ping.pong.Pong.PongDecoder
import com.onairentertainment.services.{GameService, RandomService}
import io.circe.parser
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class WebSocketSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  "WebSocketSpec" should "get ping message and return pong message" in {
    val randomService: RandomService = mock[RandomService]
    val gameService: GameService     = new GameService(randomService)
    val gameController               = new GameController(gameService)
    val wsClient: WSProbe            = WSProbe()
    WS("/ping", wsClient.flow) ~> gameController.playGame ~>
      check {
        isWebSocketUpgrade shouldEqual true

        wsClient.sendMessage("{\n  \"id\": 5,\n  \"message_type\": \"request.ping\",\n  \"timestamp\": 1234560\n}")
        val message = wsClient.expectMessage()
        val pong    = parser.parse(message.asTextMessage.getStrictText).map(_.as[Pong].toOption.get).toOption.get
        assertResult(5)(pong.id)
        assertResult("response.pong")(pong.messageType)
        assertResult(1234560)(pong.requestedAt)

        wsClient.sendCompletion()
        wsClient.expectCompletion()
      }
  }

  "WebSocketSpec" should "play game" in {

    val gameService: GameService = mock[GameService]
    val gameController           = new GameController(gameService)
    when(gameService.playGame(1)).thenReturn(Option(List(Player(1, 1, 1, 1))))
    val wsClient: WSProbe = WSProbe()
    WS("/game", wsClient.flow) ~> gameController.playGame ~>
      check {
        isWebSocketUpgrade shouldEqual true

        wsClient.sendMessage("{\n  \"numOfPlayers\": 1\n}")
        val message = wsClient.expectMessage()
        val pong    = parser.parse(message.asTextMessage.getStrictText)
        val result  = pong.toOption.get.asArray.map(_.map(_.as[Player])).get.head.toOption.get
        assertResult(Player(1, 1, 1, 1))(result)

        wsClient.sendCompletion()
        wsClient.expectCompletion()
      }
  }

}

package com.onairentertainment.controllers

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import com.onairentertainment.models.NumberOfPlayersResponse
import com.onairentertainment.models.NumberOfPlayersResponse.requestGameDecoder
import com.onairentertainment.models.ping.pong.Ping
import com.onairentertainment.models.ping.pong.Ping.PingDecoder
import com.onairentertainment.models.ping.pong.Pong.PongEncoder
import com.onairentertainment.services.GameService
import io.circe.syntax.EncoderOps
import io.circe.{parser, Json}

import scala.util.Try

class GameController(
    gameService: GameService
) {

  def playGame: Route =
    path("game") {
      handleWebSocketMessages(
        Flow.fromFunction(parseGameMessage)
      )
    } ~ path("ping") {
      handleWebSocketMessages(Flow.fromFunction(parsePingMessage))
    }

  private def parseGameMessage(msg: Message): TextMessage.Strict = {
    val decodeResult = Try(
      parser.parse(msg.asTextMessage.getStrictText).map(_.as[NumberOfPlayersResponse].toSeq).toSeq.flatten.head
    ).toEither
    val result = decodeResult match {
      case Right(value) =>
        Json.obj(
          "message_type" -> Json.fromString("response.results"),
          "results"      -> gameService.playGame(value.numOfPlayers).map(_.map(_.asJson)).map(_.asJson).getOrElse(Json.arr())
        )
      case Left(value) => ("code" -> Json.fromInt(400), "message" -> "Bad Request. Check your message:)").asJson
    }
    TextMessage.Strict(result.toString)
  }

  private def parsePingMessage(msg: Message): TextMessage.Strict = {
    val decodeResult = Try(
      parser.parse(msg.asTextMessage.getStrictText).map(_.as[Ping].toSeq).toSeq.flatten.head
    ).toEither
    val result = decodeResult match {
      case Right(value) => value.getPong.asJson
      case Left(value)  => ("code" -> Json.fromInt(400), "message" -> "Bad Request. Check your ping message:)").asJson
    }
    TextMessage.Strict(result.toString)
  }
}

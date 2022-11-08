package com.onairentertainment.models

import io.circe.Decoder

case class NumberOfPlayersResponse(numOfPlayers: Int, messageType: String)

object NumberOfPlayersResponse {
  implicit val requestGameDecoder: Decoder[NumberOfPlayersResponse] =
    Decoder.forProduct2("players", "message_type")(NumberOfPlayersResponse.apply)
}

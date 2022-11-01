package com.onairentertainment.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class NumberOfPlayersResponse(numOfPlayers: Int)

object NumberOfPlayersResponse {
  implicit val Decoder: Decoder[NumberOfPlayersResponse] = deriveDecoder[NumberOfPlayersResponse]
}

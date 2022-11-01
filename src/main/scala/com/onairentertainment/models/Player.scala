package com.onairentertainment.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Player(position: Int, result: Int, number: Int, player: Int)

object Player {
  implicit val Decoder: Decoder[Player]           = deriveDecoder[Player]
  implicit val ListDecoder: Decoder[List[Player]] = deriveDecoder[List[Player]]
  implicit val Encoder: Encoder[Player]           = deriveEncoder[Player]
  implicit val ListEncoder: Encoder[List[Player]] = deriveEncoder[List[Player]]
}

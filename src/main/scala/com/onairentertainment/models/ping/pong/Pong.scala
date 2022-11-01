package com.onairentertainment.models.ping.pong

import io.circe.{Decoder, Encoder}

case class Pong(id: Int, messageType: String, timestamp: Long, requestedAt: Long) {}

object Pong {
  val messageType = "response.pong"

  implicit val PongEncoder: Encoder[Pong] = Encoder.forProduct4("id", "message_type", "timestamp", "requested_at")(
    fields => (fields.id, fields.messageType, fields.timestamp, fields.requestedAt)
  )
  implicit val PongDecoder: Decoder[Pong] = Decoder.forProduct4("id", "message_type", "timestamp", "requested_at")(
    Pong.apply
  )
}

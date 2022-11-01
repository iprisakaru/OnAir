package com.onairentertainment.models.ping.pong

import io.circe.Decoder

import java.time.Instant

case class Ping(id: Int, messageType: String, timestamp: Long) {

  def getPong: Pong =
    Pong(id = id, messageType = Pong.messageType, timestamp = Instant.now.getEpochSecond, requestedAt = timestamp)
}

object Ping {
  implicit val PingDecoder: Decoder[Ping] =
    Decoder.forProduct3("id", "message_type", "timestamp")(Ping.apply)
}

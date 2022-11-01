package com.onairentertainment.services

import com.typesafe.config.Config

class ConfigurationService(config: Config) {

  val httpPort: Int    = config.getInt("on-air.http.port")
  val httpHost: String = config.getString("on-air.http.host")

  val maxRandomNumber: Int = config.getInt("on-air.game.max-random-number")
}

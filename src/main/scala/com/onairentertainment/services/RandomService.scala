package com.onairentertainment.services

import scala.util.Random

class RandomService(config: ConfigurationService) {

  def generateNumbersForPlayers(numOfPlayers: Int): List[Int] = {
    List.fill(numOfPlayers)(Random.nextInt(config.maxRandomNumber))
  }
}

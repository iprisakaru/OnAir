package com.onairentertainment.services

import com.onairentertainment.models.Player

class GameService(
    randomService: RandomService
) {

  def playGame(numberOfPlayers: Int): Option[List[Player]] = {
    if (numberOfPlayers >= 1) {
      val playerNumbers = randomService.generateNumbersForPlayers(numberOfPlayers)
      val playerIds     = playerNumbers.zip(1 to numberOfPlayers).toMap
      val numberResults = playerNumbers.map(countResult)
      val result        = playerNumbers.zip(numberResults).sortBy(_._2).reverse.zip(List.range(1, numberOfPlayers + 1))

      Some(result.map(data => Player(data._2, data._1._2, data._1._1, playerIds(data._1._1))))
    } else{
      Option.empty[List[Player]]
    }
  }

  private def countResult(number: Int): Int = {
    val numOfEntrances = getNumberOfEntrances(number)
    numOfEntrances.map(data => computeNumber(data._1, data._2)).sum.toInt
  }

  private def getNumberOfEntrances(number: Int): Seq[(Int, Int)] =
    number.toString.distinct.map(symbol => (symbol.asDigit, number.toString.count(_ == symbol)))

  private def computeNumber(number: Int, times: Int): Double = Math.pow(10, times - 1) * number

}

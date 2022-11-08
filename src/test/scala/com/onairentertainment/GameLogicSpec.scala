package com.onairentertainment

import com.onairentertainment.models.Player
import com.onairentertainment.services.{GameService, RandomService}
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers

class GameLogicSpec extends AnyFlatSpec with Matchers {

  val randomService: RandomService = mock[RandomService]
  val gameService: GameService     = new GameService(randomService)

  "GameService" should "not generate any results when number of players is less then 1" in {
    when(randomService.generateNumbersForPlayers(0)).thenReturn(List(0))
    val results = gameService.playGame(0)
    assert(results.isEmpty)
  }

  "GameService" should "generate numbers and counts results" in {
    when(randomService.generateNumbersForPlayers(3)).thenReturn(List(966337, 964373, 4283))
    val results = gameService.playGame(3)
    assert(results.get.nonEmpty)
    assertResult(Player(1, 20518, 966337, 1, 20412))(results.get.find(_.position == 1).get)
  }

  "GameService" should "count bonus for each number" in {
    when(randomService.generateNumbersForPlayers(4)).thenReturn(List(966337, 964373, 4283, 121283))
    val results = gameService.playGame(4)
    assert(results.get.nonEmpty)
    assertResult(Player(1, 20518, 966337, 1, 20412))(results.get.find(_.position == 1).get)
  }
}

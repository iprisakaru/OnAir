package com.onairentertainment

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.onairentertainment.controllers.GameController
import com.onairentertainment.services.{ConfigurationService, GameService, RandomService}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

object GameBootstrap extends App {

  implicit private val system: ActorSystem          = ActorSystem()
  implicit protected val executor: ExecutionContext = system.dispatcher
  val configData: ConfigurationService              = new ConfigurationService(ConfigFactory.load)

  val routes: GameController = new GameController(new GameService(new RandomService(configData)))

  println(s"Server started at ${configData.httpHost}:${configData.httpPort}")
  val binding = Http().bindAndHandle(routes.playGame, configData.httpHost, configData.httpPort)

}

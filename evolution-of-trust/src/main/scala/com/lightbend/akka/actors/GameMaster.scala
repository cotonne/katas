package com.lightbend.akka.actors

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.lightbend.akka.actors.GameMasterMessages.{Play, RoundDone, Start}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class GameMaster(players: (ActorRef, ActorRef)) extends Actor {
  implicit val timeout: Timeout = Timeout(5 seconds)

  override def receive = {
    case Start =>
      val result = for {
        a <- players._1 ? Play
        b <- players._2 ? Play
      } yield (a, b)
      val frozenSender = sender
      result onComplete { _ => frozenSender ! RoundDone }
  }
}

object GameMasterMessages {

  sealed trait GameMasterMessage

  case object Play extends GameMasterMessage

  case object Start extends GameMasterMessage

  case object RoundDone extends GameMasterMessage

  case object OtherPlayerHasCooperated extends GameMasterMessage

  case object OtherPlayerHasCheated extends GameMasterMessage

}

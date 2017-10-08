package com.lightbend.akka.actors

import akka.actor.{Actor, FSM}
import com.lightbend.akka.actors.DetectiveState._
import com.lightbend.akka.actors.GameMasterMessages._
import com.lightbend.akka.actors.messages._

class Detective extends Actor with FSM[State, Any] {
  startWith(WakedUp, OtherPlayerHasCooperated)

  when(WakedUp) {
    case Event(Play, _) =>
      sender ! Cooperate
      goto(WakedUp)
    case _ =>
      goto(FirstMoveDone)
  }

  when(FirstMoveDone) {
    case Event(Play, _) =>
      sender ! Cheat
      goto(FirstMoveDone)
    case _ =>
      goto(FirstRoundAfterCheating)
  }

  when(FirstRoundAfterCheating) {
    case Event(OtherPlayerHasCheated, _) =>
      goto(SecondRoundAfterCheating) using OtherPlayerHasCheated
    case Event(OtherPlayerHasCooperated, _) =>
      goto(SecondRoundAfterCheating)
    case _ =>
      sender ! Cooperate
      goto(FirstRoundAfterCheating)
  }

  when(SecondRoundAfterCheating) {
    case Event(OtherPlayerHasCheated, _) =>
      goto(CopyCat) using OtherPlayerHasCheated
    case Event(OtherPlayerHasCooperated, OtherPlayerHasCheated) =>
      goto(CopyCat) using OtherPlayerHasCooperated
    case Event(OtherPlayerHasCooperated, OtherPlayerHasCooperated) =>
      goto(Exploit) using OtherPlayerHasCheated
    case _ =>
      sender ! Cooperate
      goto(SecondRoundAfterCheating)
  }

  when(Exploit) {
    case Event(Play, _) =>
      sender ! Cheat
      goto(Exploit)
    case _ =>
      goto(Exploit)
  }

  when(CopyCat) {
    case Event(OtherPlayerHasCooperated, _) =>
      goto(CopyCat) using OtherPlayerHasCooperated
    case Event(OtherPlayerHasCheated, _) =>
      goto(CopyCat) using OtherPlayerHasCheated
    case Event(Play, OtherPlayerHasCheated) =>
      sender ! Cheat
      goto(CopyCat)
    case Event(Play, OtherPlayerHasCooperated) =>
      sender ! Cooperate
      goto(CopyCat)
  }
}

object DetectiveState {

  sealed trait State

  case object WakedUp extends State

  case object FirstMoveDone extends State

  case object FirstRoundAfterCheating extends State

  case object SecondRoundAfterCheating extends State

  case object Exploit extends State

  case object CopyCat extends State

}
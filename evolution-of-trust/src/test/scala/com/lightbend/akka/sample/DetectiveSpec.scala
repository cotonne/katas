package com.lightbend.akka.sample

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lightbend.akka.actors.Detective
import com.lightbend.akka.actors.GameMasterMessages._
import com.lightbend.akka.actors.messages.{Cheat, Cooperate}
import org.scalatest.WordSpecLike

class DetectiveSpec extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender {

  "Detective" should {
    "cooperate, cheat, cooperate, cooperate" in {
      val detective = system.actorOf(Props[Detective])

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cheat)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated
    }

    "cheat if you have not answered" in {
      val detective = system.actorOf(Props[Detective])

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cheat)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cheat)
      detective ! OtherPlayerHasCooperated
    }

    "act as a copycat if you have answered" in {
      val detective = system.actorOf(Props[Detective])

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cheat)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCheated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCooperated

      detective ! Play
      expectMsg(Cooperate)
      detective ! OtherPlayerHasCheated

      detective ! Play
      expectMsg(Cheat)
      detective ! OtherPlayerHasCooperated
    }
  }
}

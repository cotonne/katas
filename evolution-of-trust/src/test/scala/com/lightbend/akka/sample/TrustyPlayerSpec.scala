package com.lightbend.akka.sample

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lightbend.akka.actors.GameMasterMessages.Play
import com.lightbend.akka.actors.TrustyPlayer
import com.lightbend.akka.actors.messages.Cooperate
import org.scalatest.WordSpecLike

class TrustyPlayerSpec extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender {

  "TrustyPlayer" should {
    "always cooperate" in {
      val trustyPlayer = system.actorOf(Props[TrustyPlayer])
      trustyPlayer ! Play
      expectMsg(Cooperate)
    }
  }
}

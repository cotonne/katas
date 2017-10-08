package com.lightbend.akka.sample

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.lightbend.akka.actors.CheatyPlayer
import com.lightbend.akka.actors.GameMasterMessages.Play
import com.lightbend.akka.actors.messages.Cheat
import org.scalatest.WordSpecLike

class CheatyPlayerSpec extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with ImplicitSender {

  "CheatyPlayer" should {
    "always cheat" in {
      val player = system.actorOf(Props[CheatyPlayer])
      player ! Play
      expectMsg(Cheat)
    }
  }
}

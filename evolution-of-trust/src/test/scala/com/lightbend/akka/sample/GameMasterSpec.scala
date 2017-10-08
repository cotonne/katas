package com.lightbend.akka.sample

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActor, TestKit, TestProbe}
import akka.util.Timeout
import com.lightbend.akka.actors.GameMasterMessages._
import com.lightbend.akka.actors.{GameMaster, TrustyPlayer}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class GameMasterSpec extends TestKit(ActorSystem("test"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {
  implicit val timeout: Timeout = Timeout(1 second)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "GameMaster" should {
    "send a Play message to both players" in {
      val player1 = TestProbe("player1")
      val player2 = TestProbe("player2")
      player1.setAutoPilot((sender: ActorRef, msg: Any) => {
        sender ! Play
        TestActor.KeepRunning
      })

      val master = system.actorOf(Props(classOf[GameMaster], (player1.testActor, player2.testActor)))

      master ! Start

      player1.expectMsg(Play)
      player2.expectMsg(Play)
    }

    "wait for the players to answer" in {
      val player1 = system.actorOf(Props[TrustyPlayer])
      val player2 = system.actorOf(Props[TrustyPlayer])
      val master = system.actorOf(Props(classOf[GameMaster], (player1, player2)))

      master ! Start

      expectMsg(RoundDone)
    }
  }
}

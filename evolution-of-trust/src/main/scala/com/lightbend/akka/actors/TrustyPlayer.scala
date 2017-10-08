package com.lightbend.akka.actors

import akka.actor.Actor
import com.lightbend.akka.actors.messages.Cooperate

class TrustyPlayer extends Actor {
  override def receive = {
    case _ => sender ! Cooperate
  }
}
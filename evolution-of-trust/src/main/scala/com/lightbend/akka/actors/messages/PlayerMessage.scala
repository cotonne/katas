package com.lightbend.akka.actors.messages

sealed trait PlayerMessage

case class Cooperate() extends PlayerMessage
case class Cheat() extends PlayerMessage
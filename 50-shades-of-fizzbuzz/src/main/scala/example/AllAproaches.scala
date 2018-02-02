package example

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

// https://github.com/crista/exercises-in-programming-style

object FoldAproach {
  private def fizz(i: Int) = if (i % 3 == 0) Some("fizz") else None

  private def buzz(i: Int) = if (i % 5 == 0) Some("buzz") else None

  def apply(i: Int): String = {
    val functions: Seq[Int => Option[String]] = Seq(fizz, buzz)
    val result = functions
      .flatMap(x => x(i))
      .foldLeft(None: Option[String])((a, b) => a.map(_ + b).orElse(Some(b)))
    result.getOrElse("" + i)
  }

}

object ImperativeApproach {
  def apply(i: Int): String = {
    if (i % 15 == 0) return "fizzbuzz"
    if (i % 5 == 0) return "buzz"
    if (i % 3 == 0) return "fizz"
    "" + i
  }
}

object ChainOfResponsibilityApproach {

  case class Step(value: Int, answer: Int => String)

  case class Link(step: Step, next: Link) {
    def filter(i: Int): Boolean = i % step.value == 0

    def produce(i: Int): String = step.answer(i)
  }

  object Chain {
    def apply(link: Link, i: Int): String = if (link.filter(i)) link.produce(i) else Chain(link.next, i)
  }

  implicit class ChainImplicit(s: Step) {
    def ==>(next: Link): Link = Link(s, next)
  }

  private val links = Step(15, _ => "fizzbuzz") ==> (Step(5, _ => "buzz") ==> (Step(3, _ => "fizz") ==> (Step(1, i => "" + i) ==> null)))

  def apply(i: Int): String = Chain(links, i)
}

object PatternMatching {
  def apply(i: Int): String = i match {
    case _ if i % 15 == 0 => "fizzbuzz"
    case _ if i % 5 == 0 => "buzz"
    case _ if i % 3 == 0 => "fizz"
    case _ => "" + i
  }
}

object PartialFunctionApproach {

  private def fizzbuzz: PartialFunction[Int, String] = {
    case i if i % 15 == 0 => "fizzbuzz"
  }

  private def buzz: PartialFunction[Int, String] = {
    case i if i % 5 == 0 => "buzz"
  }

  private def fizz: PartialFunction[Int, String] = {
    case i if i % 3 == 0 => "fizz"
  }

  private def default: PartialFunction[Int, String] = {
    case i => "" + i
  }

  private val composition = fizzbuzz orElse buzz orElse fizz orElse default

  def apply(i: Int): String = composition.apply(i)
}

object FreeMonadApproach {

}

object EventBasedApproach {

  // Command Pattern?
  // Actor Pattern?
  sealed trait Command

  case class FizzBuzz() extends Command

  case class Buzz() extends Command

  case class Fizz() extends Command

  case class Default(i: Int) extends Command

}

object SideEffectApproach {
  // var au lieu de val
}

object LazyLoadingApproach {
  // lazy val
  def apply(i: Int) = ???
}

object EitherMonadApproach {
  private def fizz(i: Int) = if (i % 3 == 0) Left("fizz") else Right(i)

  private def buzz(i: Int) = if (i % 5 == 0) Left("buzz") else Right(i)

  private def fizzbuzz(i: Int) = if (i % 15 == 0) Left("fizzbuzz") else Right(i)

  private def default(i: Int) = Left("" + i)

  def apply(i: Int): String = fizzbuzz(i).flatMap(buzz).flatMap(fizz).flatMap(default) match {
    case Left(x) => x
  }
}

// Ou calcul avec une matrice?
object MachineLearningApproach {

}

object FutureApproach {
  private def fizz(i: Int) = if (i % 3 == 0) Some("fizz") else None

  private def buzz(i: Int) = if (i % 5 == 0) Some("buzz") else None

  def apply(i: Int): String = {
    val functions: Seq[Int => Option[String]] = Seq(fizz, buzz)
    val result: Future[Option[String]] = Future.sequence(functions
      .map(x => Future(x(i))))
      .map(z => z.flatten.foldLeft(None: Option[String])((a, b) => a.map(x => x + b).orElse(Some(b))))

    Await.result(result, 1 second).getOrElse("" + i)
  }
}
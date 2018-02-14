package example

import example.DDDApproach.Messages.Message

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

// https://github.com/crista/exercises-in-programming-style

object FoldApproach {
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

object FilterApproach {
  private def fizz: (Int, String) = (3, "fizz")

  private def buzz: (Int, String) = (5, "buzz")

  def apply(i: Int): String = {
    val functions: Seq[(Int, String)] = Seq(fizz, buzz)
    val result = functions
      .filter(i % _._1 == 0)
      .map(_._2(i))
      .foldLeft("")(_ + _)
    if (result.isEmpty) "" + i else result
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
// Java Enum approach?


// Même chose que la monad?
// S'arrête si on trouve le résultat
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

object PatternMatchingApproach {
  def apply(i: Int): String = i match {
    case _ if i % 15 == 0 => "fizzbuzz"
    case _ if i % 5 == 0 => "buzz"
    case _ if i % 3 == 0 => "fizz"
    case _ => "" + i
  }
}

object PatternMatchingOtherApproach {
  def apply(i: Int): String = i match {
    case n if n % 15 == 0 => "fizzbuzz"
    case n if n % 5 == 0 => "buzz"
    case n if n % 3 == 0 => "fizz"
    case n => "" + n
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

object EventBasedApproach {

  // Command Pattern?
  // Actor Pattern? (4 actors et s'échangent entre eux
  sealed trait Command

  case class FizzBuzz() extends Command

  case class Buzz() extends Command

  case class Fizz() extends Command

  case class Default(i: Int) extends Command

}

object EitherMonadApproach {
  private def fizz(i: Int) = if (i % 3 == 0) Left("fizz") else Right(i)

  private def buzz(i: Int) = if (i % 5 == 0) Left("buzz") else Right(i)

  private def fizzbuzz(i: Int) = if (i % 15 == 0) Left("fizzbuzz") else Right(i)

  private def default(i: Int) = Left("" + i)

  // Remplacable par un fold
  def apply(i: Int): String = fizzbuzz(i).flatMap(buzz).flatMap(fizz).flatMap(default) match {
    case Left(x) => x
  }
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

object PolymorphicApproach {

  sealed trait Word {
    def say(i: Int): String
  }

  class Fizz extends Word {
    override def say(i: Int): String = "fizz"
  }

  class Buzz extends Word {
    override def say(i: Int): String = "buzz"
  }

  class FizzBuzz extends Word {
    override def say(i: Int): String = "fizzbuzz"
  }

  class Default extends Word {
    override def say(i: Int): String = "" + i
  }

  def factory(i: Int): Word = i match {
    case _ if i % 15 == 0 => new FizzBuzz
    case _ if i % 5 == 0 => new Buzz
    case _ if i % 3 == 0 => new Fizz
    case _ => new Default
  }

  def apply(i: Int): String = factory(i).say(i)
}

object DDDApproach {

  // object callisthenics Approach?
  case class Number(i: Int) {
    def isMultipleOf(multiple: Number): Boolean = i % multiple.i == 0

    def asMessage = Message(i.toString)
  }

  object Messages {

    sealed case class Message(word: String)

    val FIZZ = Message("fizz")
    val BUZZ = Message("buzz")
    val FIZZBUZZ = Message("fizzbuzz")
  }

  case class Word(multiple: Number, message: Message) {
    def isMultipleOf(number: Number): Boolean = number.isMultipleOf(multiple)
  }

  private val fizz = Word(Number(3), Messages.FIZZ)
  private val buzz = Word(Number(5), Messages.BUZZ)
  private val fizzbuzz = Word(Number(15), Messages.FIZZBUZZ)


  def apply(number: Number): Message = number match {
    case _ if fizzbuzz.isMultipleOf(number) => fizzbuzz.message
    case _ if buzz.isMultipleOf(number) => buzz.message
    case _ if fizz.isMultipleOf(number) => fizz.message
    case _ => number.asMessage
  }

  def apply(i: Int): String = DDDApproach(Number(i)).word
}

object ExceptionApproach {

}

// Stream approach
// On traite les 4 et on prend le premier
object GenerateAllAndTakeFirstApproach {

}

object ParserCombinatorApproach {
  // (buzz, i)
  //

  // def x : Parser
}

object ActorBasedApproach {

}

object MachineLearningApproach {
  // Ou calcul avec une matrice?
}

// Procedural approach?
object SideEffectApproach {
  // var au lieu de val
}

object LazyLoadingApproach {
  // lazy val
  def apply(i: Int) = ???
}

object FreeMonadApproach {

}

object DecoratorApproach {
  // x -> if(x) => modify, call next
}

object StateApproach {
  // Fizz -> FizzBuzz
}

object TreeVisitorApproach {

}

object EnterpriseApproach {

}

object ProbabilisticApproach {

}

object XtremConstraintsApproach {
  // No If
  // No Primitive
  // Only Void
}

package example

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1


class FizzBuzzTest extends FlatSpec with Matchers {
  val approachs: TableFor1[Int => String] = Table(
    ("approach")
    , (FoldAproach.apply)
    , (ImperativeApproach.apply)
    , (ChainOfResponsibilityApproach.apply)
    , (PatternMatching.apply)
    , (PartialFunctionApproach.apply)
    , (EitherMonadApproach.apply)
    , (FutureApproach.apply)
  )

  forAll(approachs) {
    (approach: Int => String) =>
      approach(1) shouldEqual "1"
      approach(3) shouldEqual "fizz"
      approach(5) shouldEqual "buzz"
      approach(15) shouldEqual "fizzbuzz"
  }
}

package example

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor1


class FizzBuzzTest extends FlatSpec with Matchers {
  "All approaches" should "succeed" in {
    val approaches: TableFor1[Int => String] = Table(
      "approach"
      , FoldApproach.apply
      , ImperativeApproach.apply
      , ChainOfResponsibilityApproach.apply
      , PatternMatchingApproach.apply
      , PartialFunctionApproach.apply
      , EitherMonadApproach.apply
      , FutureApproach.apply
      , PolymorphicApproach.apply
      , FilterApproach.apply
      , PatternMatchingOtherApproach.apply
      , DDDApproach.apply
    )

    forAll(approaches) {
      (approach: Int => String) =>
        approach(1) shouldEqual "1"
        approach(3) shouldEqual "fizz"
        approach(5) shouldEqual "buzz"
        approach(15) shouldEqual "fizzbuzz"
    }
  }
}

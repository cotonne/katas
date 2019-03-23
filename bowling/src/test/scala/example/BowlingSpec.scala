package example

import org.scalatest._
import org.scalatest.Matchers._

// Que des gouttières 0 => 0
// Que des gouttières sauf 1 => 1
// Que des gouttières sauf une frame a (1,1) => 2
// Que des gouttières sauf deux frame a (1,1) => 4
// 1 Spare
// 1 Strike
// Que des strike => 300

class BowlingSpec extends FunSpec {

  describe("Bowling") {
    describe("score") {
      val Gutter = Frame(0, 0)
      it("should be 0 given there are only gutters") {
        val game = Game(
          Gutter, Gutter, Gutter, Gutter,
          Gutter, Gutter, Gutter, Gutter,
          Gutter, Gutter)
        val score: Int = Bowling.score(game)
        score shouldBe 0
      }
      it("should be the number of pins knocked down during the game") {
        val game = Game(
          Frame(1, 0), Frame(1, 1), Gutter, Gutter,
          Gutter, Gutter, Gutter, Gutter,
          Gutter, Gutter)
        val score: Int = Bowling.score(game)
        score shouldBe 3
      }

      describe("spare") {
        it("should add a bonus equals to the next try after a spare") {
          val game = Game(
            Frame(9, 1), Frame(1, 0), Gutter, Gutter,
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Gutter)
          val score: Int = Bowling.score(game)
          score shouldBe 12
        }

        it("should not have a bonus given the last frame is a spare") {
          val game = Game(
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Frame(9, 1))
          val score: Int = Bowling.score(game)
          score shouldBe 10
        }
      }

      describe("strike") {
        it("should add a bonus equals to the two next trys after a strike") {
          val game = Game(
            Strike, Frame(1, 1), Gutter, Gutter,
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Gutter)
          val score: Int = Bowling.score(game)
          score shouldBe 14
        }

        it("should add a bonus with multiple strikes") {
          val game = Game(
            Strike, Strike, Strike, Gutter,
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Gutter)
          val score: Int = Bowling.score(game)
          score shouldBe (10 + 10 + 10) + (10 + 10) + (10)
        }

        it("should not add a bonus when the last frame is a strike") {
          val game = Game(
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Gutter, Gutter, Gutter,
            Gutter, Strike)
          val score: Int = Bowling.score(game)
          score shouldBe 10
        }

      }

      it("should give 300 for a perfect score") {
        val game = Game(
          Strike, Strike, Strike, Strike,
          Strike, Strike, Strike, Strike,
          Strike, Strike, Strike)
        val score: Int = Bowling.score(game)
        score shouldBe 300
      }
    }
  }
}

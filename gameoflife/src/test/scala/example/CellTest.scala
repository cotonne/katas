package example

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class CellTest extends FunSuite {

  test("should calculate neighbours in one dimension") {
    val cell = Cell(0)
    val neighbours = cell.neighbours()
    neighbours shouldBe Set(Cell(-1), Cell(1))
  }
  test("should calculate neighbours in two dimension") {
    val cell = Cell(0, 0)
    val neighbours = cell.neighbours()
    neighbours shouldBe Set(
      Cell( 1, -1), Cell( 1, 0), Cell( 1, 1),
      Cell( 0, -1), /*        */ Cell( 0, 1),
      Cell(-1, -1), Cell(-1, 0), Cell(-1, 1),
    )
  }
}

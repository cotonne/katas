package example

import org.scalatest._
import org.scalatest.Matchers._

// Infinite size
// Infinite dimension
// High performance

class GameOfLifeSpec extends FreeSpec {

  "GameOfLife" - {
    "tick" - {
      "in zero dimension" - {
        "should stay empty" in {
          val board = Board(Set())
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set())
        }

      }
      "in one dimension" - {
        "should have only one cell after a tick given there is a cell with two neighbours" in {
          val board = Board(Set(Cell(-1), Cell(0), Cell(1)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set(Cell(0)))
        }

        "should keep cells alive after a tick given the cell has two neighbours" in {
          val board = Board(Set(Cell(-1), Cell(0), Cell(1), Cell(2)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set(Cell(0), Cell(1)))
        }
      }
      "in N-dimension" - {
        "should calculate alive cells with all dimensions" in {
          val board = Board(Set(Cell(-1, -1), Cell(0, 0), Cell(1, 2)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set())
        }

        "should kill a cell without neighbours" in {
          val board = Board(Set(Cell(0, 0)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set())
        }

        "should keep cells alive after a tick given the cell has two neighbours" in {
          val board = Board(Set(Cell(-1, -1), Cell(0, 0), Cell(1, 0)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set(Cell(0, 0)))
        }

        "should keep cells alive after a tick given the cell has three neighbours" in {
          val board = Board(Set(Cell(1, 1),
            Cell(-1, 0), Cell(0, 0),
            Cell(-1, -1)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set(Cell(-1, -1), Cell(-1, 0), Cell(0, 0)))
        }

        "should kill with four neighbours or more" in {
          val board = Board(Set(Cell(-1, -1), Cell(1, 1), Cell(0, 0), Cell(-1, 1), Cell(1, -1)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set())
        }

        "should revive a dead cell with four neighbours " in {
          val board = Board(Set(Cell(-1, -1), Cell(1, 1), Cell(-1, 1), Cell(1, -1)))
          val newBoard = GameOfLife.tick(board)
          newBoard shouldBe Board(Set(Cell(0, 0)))
        }
      }
    }
  }
}




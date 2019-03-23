package example

object GameOfLife {
  def tick(board: Board): Board = board.tick
}

case class Board(cells: Set[Cell]) {
  private val DISTANCE_TO_BE_A_NEIGHBOUR = 1
  private val TO_STAY_ALIVE = Seq(2, 3)
  private val TO_RESUSCITATE = 4

  def tick: Board = {
    val newCells = cells.flatMap(_.neighbours())
      .filter(hasEnoughNeighbours(TO_RESUSCITATE))
      .diff(cells)
    Board(cells.filter(hasEnoughNeighbours(TO_STAY_ALIVE: _*)) ++ newCells)
  }

  private def hasEnoughNeighbours(quantity: Int*)(cell: Cell) = quantity.contains(calculateNeighbours(cell))

  private def calculateNeighbours(cell: Cell): Int = cells.count(_.<->(cell) == DISTANCE_TO_BE_A_NEIGHBOUR)
}

case class Cell(position: Int*) {
  private val NEIGHBOUR = Set(-1, 0, 1)

  def neighbours(): Set[Cell] = {
    var deltas = Set(Geometry.Vector())
    for (_ <- 0 until position.size) {
      deltas = NEIGHBOUR.flatMap(x => deltas.map(vector => vector.addDimension(x)))
    }

    val cells = deltas.map(delta => {
      val vector = Geometry.Vector(position: _*).translate(delta)
      Cell(vector.dimensions: _*)
    })
    cells diff Set(this)
  }

  def <->(other: Cell): Int = position.zip(other.position).map { case (first, second) => Math.abs(first - second) }.max
}

object Geometry {

  case class Vector(dimensions: Int*) {
    def translate(delta: Vector): Vector = Vector(dimensions.zip(delta.dimensions).map { case (i, j) => i + j }: _*)

    def addDimension(x: Int): Vector = Vector(x +: dimensions: _*)
  }

}

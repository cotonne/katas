package example

import example.Bowling.NUMBER_OF_PINS

object Bowling {
  def score(game: Game): Int = game.sum

  val NUMBER_OF_PINS = 10
}

case class Game(frames: Framable*) {
  def sum: Int = countBonus(frames).sum

  private def countBonus(frames: Seq[Framable]): Seq[Int] = {
    if (frames == Nil)
      Nil
    else {
      val frameScore: Int = frames match {
        case Strike +: tail => NUMBER_OF_PINS + takeNextTry(tail, 2)
        case frame +: tail if frame.isSpare => NUMBER_OF_PINS + takeNextTry(tail, 1)
        case frame +: _ => frame.total
      }
      frameScore +: countBonus(frames.tail)
    }
  }

  private def takeNextTry(tail: Seq[Framable], numberOfTry: Int) = {
    tail.flatMap(_.toSeq).take(numberOfTry).sum
  }
}

trait Framable {
  def toSeq: Seq[Int]

  def total: Int

  def isSpare: Boolean
}

case class Frame(firstTry: Int, secondTry: Int) extends Framable {
  def isSpare: Boolean = firstTry + secondTry == 10

  def total: Int = firstTry + secondTry

  def toSeq: Seq[Int] = Seq(firstTry, secondTry)
}

object Strike extends Framable {
  override def toSeq: Seq[Int] = Seq(NUMBER_OF_PINS)

  override def total: Int = NUMBER_OF_PINS

  override def isSpare: Boolean = false
}

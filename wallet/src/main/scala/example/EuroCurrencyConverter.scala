package example

import example.currencies._

case class EuroCurrencyConverter(tuple: (Currency, Double)*) {
  val rates = (tuple :+ (EURO -> 1.0)).toMap

  def toEuro(money: Money): Money = Money(money.value * rates(money.currency), EURO)
}

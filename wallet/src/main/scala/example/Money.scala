package example

import example.currencies._

import scala.math.BigDecimal.RoundingMode

case class Value(value: BigDecimal) {
  def *(factor: BigDecimal) = Value(factor * value)

  def +(other: Value): Value = Value(value + other.value)

  def round = Value(value.setScale(2, RoundingMode.DOWN))

  override def toString: String = "" + value
}

object Value {
  def apply(value: Int) = new Value(BigDecimal(value))
}

case class Money(value: Value, currency: Currency) {
  override def toString: String = value + " " + currency

  def +(other: Money): Money = Money(value + other.value, currency)

  def round = Money(value.round, currency)
}

object currencies {

  sealed trait Currency

  case object EURO extends Currency

  case object WON extends Currency

  case object DINAR extends Currency

  case object FORINT extends Currency

  case object US$ extends Currency

  case object HK$ extends Currency

  case object CZK extends Currency

  case object YEN extends Currency

  case object POUND extends Currency

}

object Money {

  implicit class Builder(val value: Double) {
    def euro = Money(Value(value), EURO)

    def won = Money(Value(value), WON)

    def pences = Money(Value(value), DINAR)

    def dinar = Money(Value(value), DINAR)

    def forint = Money(Value(value), FORINT)

    def us$ = Money(Value(value), US$)

    def hk$ = Money(Value(value), HK$)

    def czk = Money(Value(value), CZK)

    def yen = Money(Value(value), YEN)

    def ukp = Money(Value(value), POUND)
  }

}

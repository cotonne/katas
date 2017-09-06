package example

import example.Money.Builder

case class Wallet(moneys: Money*) {
  def evaluate(convert: EuroCurrencyConverter): Money = moneys
    .map(convert.toEuro)
    .foldLeft(0 euro)((a, b) => a + b)
}

package example

import example.Money.Builder
import example.currencies._
import org.scalatest._

class WalletSpec extends WordSpec with Matchers {

  "The wallet" should {
    val converter: EuroCurrencyConverter = EuroCurrencyConverter(
      DINAR -> 0.00072,
      WON -> 0.00075,
      FORINT -> 0.0033,
      POUND -> 0.0109,
      CZK -> 0.03831,
      HK$ -> 0.11,
      US$ -> 0.84,
      YEN -> 0.0076)

    "give a valuation of 0 euro when empty" in {
      val myWallet: Wallet = Wallet()
      myWallet.evaluate(converter) shouldEqual (0 euro)
    }

    "give his valuation when containing only one currency" in {
      val myWallet: Wallet = Wallet(10 euro)
      myWallet.evaluate(converter) shouldEqual (10 euro)
    }

    "add money" in {
      val myWallet: Wallet = Wallet(10 euro, 1 euro)
      myWallet.evaluate(converter) shouldEqual (11 euro)
    }

    "add money in differents currencies" in {
      val myWallet: Wallet = Wallet(10 euro, 1 us$)
      myWallet.evaluate(converter) shouldEqual (10.84 euro)
    }

    "add a lot of money in differents currencies" in {
      val myWallet: Wallet = Wallet(10 euro, 1 us$, 1000 won)
      myWallet.evaluate(converter) shouldEqual (11.59 euro)
    }

    "evalute the correct amount" in {
      val myWallet: Wallet = Wallet(50 dinar, 1170 won, 10010 forint,
        25 ukp, 0.10 hk$, 2 czk, 0.11 us$, 11 yen, 0.05 euro)
      myWallet.evaluate(converter).round shouldEqual (34.53 euro)
    }
  }
}

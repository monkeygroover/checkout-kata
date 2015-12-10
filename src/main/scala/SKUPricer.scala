/**
  * Created by monkeygroover on 09/12/15.
  */
import cats.state._
import cats.std.all._

// rules, take a count, apply a rule and then pass the remaining count on to the next rule
// returning the whole count at the end

case class SKUPricer() {
  import SKUPricer._
  def getPrice(itemCount: Int): Int = {

    val priceCalculator = for {
      sp <- specialPricer(3, 120)
      up <- unitPricer(20)
    } yield (sp + up)

    priceCalculator.runA(itemCount).run
  }
}

object SKUPricer {
  type PriceState = State[Int, Int]

  def specialPricer(groupSize: Int, discountPrice: Int): PriceState = State(items => (items % groupSize, items / groupSize * discountPrice) )

  def unitPricer(unitPrice: Int): PriceState = State(items => (0, items * unitPrice))
}










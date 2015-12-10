/**
  * Created by monkeygroover on 09/12/15.
  */

import SKUPricer._
import cats.state._
import cats.std.all._
import cats.syntax.traverse._

case class SKUPricer(pricingRules: List[PriceRule]) {
  def getPrice(itemCount: Int): Int =
  // run the rules in order, then sum all the results
    pricingRules.sequenceU.runA(itemCount).run.reduce(_ + _)
}

object SKUPricer {
  // pricing rules use a State monad to pass the remaining item count along the chain of rules
  type PriceRule = State[Int, Int]

  def specialPricer(groupSize: Int, discountPrice: Int): PriceRule = State(items => (items % groupSize, items / groupSize * discountPrice) )

  def unitPricer(unitPrice: Int): PriceRule = State(items => (0, items * unitPrice))
}










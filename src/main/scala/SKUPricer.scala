/**
  * Created by monkeygroover on 09/12/15.
  */

import SKUPricer._
import scalaz._
import Scalaz._

trait SKUPricer{
  def getPrice(itemCount: Int): Int
}

case class SKULinearPricer(pricingRules: List[PriceRule]) extends SKUPricer {
  def getPrice(itemCount: Int): Int =
  // run the rules in order, then sum all the results
    pricingRules.sequenceU.eval(itemCount).sum
}

case class SKUBestPricer(pricingRules: List[PriceRule]) extends SKUPricer {
  def getPrice(itemCount: Int): Int =
    // run the rules in all permutations and return the smallest result
    pricingRules.permutations.map {
      _.sequenceU.eval(itemCount).sum
    }.min
}

object SKUPricer {
  // pricing rules use a State monad to pass the remaining item count along the chain of rules
  type PriceRule = State[Int, Int]

  def specialPriceRule(groupSize: Int, discountPrice: Int): PriceRule = State(items => (items % groupSize, items / groupSize * discountPrice) )

  def unitPriceRule(unitPrice: Int): PriceRule = State(items => (0, items * unitPrice))
}










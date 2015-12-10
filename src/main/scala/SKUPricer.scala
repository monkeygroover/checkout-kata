/**
  * Created by monkeygroover on 09/12/15.
  */

import SKUPricer._
import cats.state._
import cats.std.all._
import cats.syntax.traverse._

// rules, take a count, apply a rule and then pass the remaining count on to the next rule
// returning the whole count at the end

case class SKUPricer(pricingRules: List[PriceState]) {
  def getPrice(itemCount: Int): Int = {

    val ruleSequence = pricingRules.sequenceU

    ruleSequence.runA(itemCount).run.reduce(_+_)
  }
}

object SKUPricer {
  type PriceState = State[Int, Int]

  def specialPricer(groupSize: Int, discountPrice: Int): PriceState = State(items => (items % groupSize, items / groupSize * discountPrice) )

  def unitPricer(unitPrice: Int): PriceState = State(items => (0, items * unitPrice))
}










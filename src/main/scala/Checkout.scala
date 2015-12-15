/**
  * Created by monkeygroover on 09/12/15.
  */

import scalaz._
import Scalaz._

trait Checkout {
  val checkout: Map[String, SKUPricer] => List[String] => ValidationNel[String, Int]
}

object Checkout extends Checkout {
  val checkout = (skuRules: Map[String, SKUPricer]) => (items: List[String]) =>
    items.foldMap(x => Map(x -> 1))  // create a map of SKU -> count of 'scanned' items
      .map { case (sku, skuCount) =>
      // get the rules for the SKU (if they exist) and map them to get the results for each SKU group
        skuRules.get(sku).map(rule => (rule.getPrice(skuCount)).success)
          .getOrElse(s"'$sku' rule not found".failureNel)
        }
      .reduce(_ |+| _) // sum the results for each SKU to get the final total
}
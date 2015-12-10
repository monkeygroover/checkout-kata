/**
  * Created by monkeygroover on 09/12/15.
  */

import cats.std.all._
import cats.data.Xor
import cats.syntax.semigroup._
import cats.syntax.foldable._

trait Checkout {
  val checkout: Map[String, SKUPricer] => List[String] => Xor[String, Int]
}

object Checkout extends Checkout {
  val checkout = (skuRules: Map[String, SKUPricer]) => (items: List[String]) =>
    items.foldMap(x => Map(x -> 1))  // count how many of each "SKU" have been scanned
      .map {
      case (sku, skuCount) =>
        skuRules.get(sku) match {
          case Some(rule) => Xor.right(rule.getPrice(skuCount))
          case None => Xor.left[String, Int](s"'$sku' rule not found")
        }
    } // get the rules for the SKU and run it
      .reduce(_ |+| _) // sum the results for each SKU
}
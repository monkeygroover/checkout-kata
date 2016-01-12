/**
  * Created by monkeygroover on 09/12/15.
  */

import cats.Semigroup
import cats.SemigroupK
import cats.std.all._
import cats.data.{Validated, NonEmptyList, ValidatedNel}
import cats.syntax.semigroup._
import cats.syntax.foldable._
import cats.syntax.validated._

trait TotalCalculator {
  val calculateTotal: List[String] => ValidatedNel[String, Int]
}

case class Checkout(skuRules: Map[String, SKUPricer]) extends TotalCalculator {

  val calculateTotal = (items: List[String]) =>
    items.foldMap(x => Map(x -> 1))  // create a map of SKU -> count of 'scanned' items
      .map { case (sku, skuCount) =>
      // get the rules for the SKU (if they exist) and map them to get the results for each SKU group
        skuRules.get(sku).map(rule => rule.getPrice(skuCount).valid)
          .getOrElse(s"'$sku' rule not found".invalidNel)
        }
      .reduce(_ |+| _) // sum the results for each SKU to get the final total
}

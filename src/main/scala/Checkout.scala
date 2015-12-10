
import cats.std.all._
import cats.syntax.semigroup._
/**
  * Created by monkeygroover on 09/12/15.
  */

trait Checkout {
  val checkout: Map[String, SKUPricer] => List[String] => Option[Int]
}

object Checkout extends Checkout {
  val checkout = (rules: Map[String, SKUPricer]) => (items: List[String]) =>
    items.groupBy(identity).mapValues(_.size)
      .map{ case (sku, itemCount) => rules.get(sku).map(_.getPrice(itemCount))}
      .reduce(_ |+| _)
}
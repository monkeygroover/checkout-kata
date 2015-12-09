/**
  * Created by monkeygroover on 09/12/15.
  */
//
import cats._, cats.state._, cats.std.all._

// rules, take a count, apply a rule and then pass the remaining count on to the next rule
// returning the whole count at the end

trait SKURules {
  def getPrice(items: Int) : Int
}

case class ItemPricer(unitPrice: Int) extends SKURules{
  def getPrice(items: Int) = items * unitPrice
}


//def specialPrice(groupSize: Int, groupPrice: Int) = State[Int, Int] {
//  case x =>
//}
//
//
//def getPrice(items: Int) = for {
//
//}











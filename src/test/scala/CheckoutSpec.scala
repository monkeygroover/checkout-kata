/**
  * Created by monkeygroover on 10/12/15.
  */

import cats.data.Xor
import org.scalatest.{Matchers, FlatSpec}

class CheckoutSpec
  extends FlatSpec
  with Matchers {

  import Checkout._, SKUPricer._

  val PricingData = Map(
    "A" -> SKUPricer(specialPricer(3, 130) :: unitPricer(50) :: Nil),
    "B" -> SKUPricer(specialPricer(2, 45) :: unitPricer(30) :: Nil),
    "C" -> SKUPricer(unitPricer(20) :: Nil),
    "D" -> SKUPricer(unitPricer(15) :: Nil)
  )

  "Checkout" should "return expected result provided pricing information and single A item" in {
    val Items = "A" :: Nil

    val ExpectedResult = Xor.right(50)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should "return expected result provided pricing information and 2 A items" in {
    val Items = "A" :: "A" :: Nil

    val ExpectedResult = Xor.right(100)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should "return expected result provided pricing information and 3 A items" in {
    val Items = "A" :: "A" :: "A" :: Nil

    val ExpectedResult = Xor.right(130)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should "return expected result provided pricing information and 4 A items" in {
    val Items = "A" :: "A" :: "A" :: "A" :: Nil

    val ExpectedResult = Xor.right(180)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should "return expected result provided pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "A" :: "C" :: "A" :: "B" :: "B" :: "B" :: Nil

    val ExpectedResult = Xor.right(305)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should "return a failure if an unexpected item is provided" in {
    val Items = "A" :: "K" :: "B" :: "B" :: Nil

    checkout(PricingData)(Items) shouldBe Xor.left("'K' rule not found")
  }
}

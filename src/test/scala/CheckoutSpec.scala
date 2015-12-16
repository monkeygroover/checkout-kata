/**
  * Created by monkeygroover on 10/12/15.
  */

import scalaz._
import Scalaz._
import org.scalatest.{Matchers, FlatSpec}

class CheckoutSpec
  extends FlatSpec
  with Matchers {

  import SKUPricer._

  val PricingData = Map(
    "A" -> SKULinearPricer(specialPriceRule(3, 130) :: unitPriceRule(50) :: Nil),
    "B" -> SKULinearPricer(specialPriceRule(2, 45) :: unitPriceRule(30) :: Nil),
    "C" -> SKULinearPricer(unitPriceRule(20) :: Nil),
    "D" -> SKULinearPricer(unitPriceRule(15) :: Nil)
  )

  val checkout = Checkout(PricingData)

  "Checkout" should "return expected result provided pricing information and single A item" in {
    val Items = "A" :: Nil

    checkout.calculateTotal(Items) shouldBe 50.success
  }

  "Checkout" should "return expected result provided pricing information and 2 A items" in {
    val Items = "A" :: "A" :: Nil

    checkout.calculateTotal(Items) shouldBe 100.success
  }

  "Checkout" should "return expected result provided pricing information and 3 A items" in {
    val Items = "A" :: "A" :: "A" :: Nil

    checkout.calculateTotal(Items) shouldBe 130.success
  }

  "Checkout" should "return expected result provided pricing information and 4 A items" in {
    val Items = "A" :: "A" :: "A" :: "A" :: Nil

    checkout.calculateTotal(Items) shouldBe 180.success
  }

  "Checkout" should "return expected result provided pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "A" :: "C" :: "A" :: "B" :: "B" :: "B" :: Nil

    checkout.calculateTotal(Items) shouldBe 305.success
  }

  "Checkout" should "return expected result provided different pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "C" :: "A" :: "C" :: "A" :: "B" :: "B" :: Nil

    val AlternativePricingData = Map(
      "A" -> SKULinearPricer(specialPriceRule(3, 120) :: unitPriceRule(70) :: Nil),
      "B" -> SKULinearPricer(specialPriceRule(2, 70) :: unitPriceRule(40) :: Nil),
      "C" -> SKULinearPricer(unitPriceRule(25) :: Nil),
      "D" -> SKULinearPricer(unitPriceRule(16) :: Nil)
    )

    Checkout(AlternativePricingData).calculateTotal(Items) shouldBe (190 + 110 + 50 + 16).success
  }

  "Checkout" should "return a failure if an unexpected item is provided" in {
    val Items = "A" :: "K" :: "B" :: "B" :: Nil

    checkout.calculateTotal(Items) shouldBe NonEmptyList("'K' rule not found").failure
  }

  "Checkout" should "return failures if multiple unexpected items are provided" in {
    val Items = "A" :: "K" :: "B" :: "B" :: "X" :: Nil

    checkout.calculateTotal(Items) shouldBe NonEmptyList("'K' rule not found", "'X' rule not found").failure
  }

  "Checkout" should "not rip people off, if a 'BestPricer' is used :)" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "C" :: "A" :: "C" :: "A" :: "B" :: "B" :: Nil

    val DodgyPricingData = Map(
      "A" -> SKUBestPricer(specialPriceRule(3, 120) :: unitPriceRule(10) :: Nil),
      "B" -> SKUBestPricer(unitPriceRule(10) :: specialPriceRule(2, 70) :: Nil),
      "C" -> SKUBestPricer(unitPriceRule(25) :: Nil),
      "D" -> SKUBestPricer(unitPriceRule(16) :: Nil)
    )

    Checkout(DodgyPricingData).calculateTotal(Items) shouldBe (40 + 30 + 50 + 16).success
  }

}

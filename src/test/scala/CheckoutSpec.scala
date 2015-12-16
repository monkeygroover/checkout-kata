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
    "A" -> SKUPricer(specialPricer(3, 130) :: unitPricer(50) :: Nil),
    "B" -> SKUPricer(specialPricer(2, 45) :: unitPricer(30) :: Nil),
    "C" -> SKUPricer(unitPricer(20) :: Nil),
    "D" -> SKUPricer(unitPricer(15) :: Nil)
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
      "A" -> SKUPricer(specialPricer(3, 120) :: unitPricer(70) :: Nil),
      "B" -> SKUPricer(specialPricer(2, 70) :: unitPricer(40) :: Nil),
      "C" -> SKUPricer(unitPricer(25) :: Nil),
      "D" -> SKUPricer(unitPricer(16) :: Nil)
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
}

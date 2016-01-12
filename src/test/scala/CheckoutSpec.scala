/**
  * Created by monkeygroover on 10/12/15.
  */

import cats.data.Xor
import cats.syntax.validated._
import org.scalatest.{FlatSpec, Matchers}

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

  "Checkout" should "return expected result provided pricing information and single A item" in {
    val Items = "A" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe 50.valid
  }

  "Checkout" should "return expected result provided pricing information and 2 A items" in {
    val Items = "A" :: "A" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe 100.valid
  }

  "Checkout" should "return expected result provided pricing information and 3 A items" in {
    val Items = "A" :: "A" :: "A" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe 130.valid
  }

  "Checkout" should "return expected result provided pricing information and 4 A items" in {
    val Items = "A" :: "A" :: "A" :: "A" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe 180.valid
  }

  "Checkout" should "return expected result provided pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "A" :: "C" :: "A" :: "B" :: "B" :: "B" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe 305.valid
  }

  "Checkout" should "return expected result provided different pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "C" :: "A" :: "C" :: "A" :: "B" :: "B" :: Nil

    val AlternativePricingData = Map(
      "A" -> SKUPricer(specialPricer(3, 120) :: unitPricer(70) :: Nil),
      "B" -> SKUPricer(specialPricer(2, 70) :: unitPricer(40) :: Nil),
      "C" -> SKUPricer(unitPricer(25) :: Nil),
      "D" -> SKUPricer(unitPricer(16) :: Nil)
    )

    Checkout(AlternativePricingData).calculateTotal(Items) shouldBe (190 + 110 + 50 + 16).valid
  }

  "Checkout" should "return a failure if an unexpected item is provided" in {
    val Items = "A" :: "K" :: "B" :: "B" :: Nil

    Checkout(PricingData).calculateTotal(Items) shouldBe "'K' rule not found".invalidNel
  }
}

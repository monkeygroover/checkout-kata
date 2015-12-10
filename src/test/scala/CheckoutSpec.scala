/**
  * Created by monkeygroover on 10/12/15.
  */
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

  "Checkout" should s"return expected result provided pricing information and single A item" in {
    val Items = "A" :: Nil

    val ExpectedResult = Some(50)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should s"return expected result provided pricing information and 2 A items" in {
    val Items = "A" :: "A" :: Nil

    val ExpectedResult = Some(100)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should s"return expected result provided pricing information and 3 A items" in {
    val Items = "A" :: "A" :: "A" :: Nil

    val ExpectedResult = Some(130)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should s"return expected result provided pricing information and 4 A items" in {
    val Items = "A" :: "A" :: "A" :: "A" :: Nil

    val ExpectedResult = Some(180)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }

  "Checkout" should s"return expected result provided pricing information and a complex list of items" in {
    val Items = "A" :: "A" :: "D" :: "B" :: "A" :: "C" :: "A" :: "B" :: "B" :: "B" :: Nil

    val ExpectedResult = Some(305)

    checkout(PricingData)(Items) shouldBe ExpectedResult
  }
}

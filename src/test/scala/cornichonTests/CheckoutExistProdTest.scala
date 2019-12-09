package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutExistProdTest extends CheckoutTest with FeatureWithToken with FeatureWithProduct {

  override lazy val baseUrl = apiUrl + s"/$projectKey"

  override def feature: FeatureDef = Feature("Checkout process for existing product") {
    Scenario("Add product | Create order | Cleanup") {
      WithToken {
        Then I createTaxCategory
        Then I AddProduct
        Then I createCart
        Then I addLineItem
        Then I createOrderFromCart
        Then I getCartById
        Then I deleteOrderFromCart
        Then I deleteCart
        Then I DeleteProduct
      }
    }
  }

  override def createTaxCategory =
    Attach {
      When I post(s"$apiUrl/$projectKey/tax-categories")
        .withBody("""
          |{
          |  "name" : "test-tax-category-<random-positive-integer>",
          |  "rates" : [ {
          |    "name" : "test-tax-category",
          |    "amount" : 0.2,
          |    "includedInPrice" : true,
          |    "country" : "DE"
          |  } ]
          |}
          |""".stripMargin)
      Then I save_body_path("id" -> "taxCategoryId")
      Then I save_body_path("version" -> "taxCategoryVersion")
      Then assert status.is(201)
    }

  def addLineItem =
    Attach {
      When I post("/carts/<cartId>").withBody("""
          |{
          |    "version": <cartVersion>,
          |    "actions": [
          |        {
          |            "action" : "addLineItem",
          |            "productId" : "<productId>"
          |        },
          |        {
          |         	  "action" : "setShippingAddress",
          |         	  "address": {
          |             	 	"key": "test_address",
          |         				"company": "commercetools GmbH",
          |         				"streetName": "Sonnenallee",
          |         				"streetNumber": "223",
          |         				"city": "Berlin",
          |         				"postalCode": "12059",
          |         				"country": "DE"
          |             }
          |        }
          |    ]
          |}
          |""".stripMargin)
      Then I save_body_path("version" -> "cartVersion")
      And assert status.is(200)
    }

  override def createOrderFromCart =
    Attach {
      When I post("/orders").withBody("""
            |{
            |  "id" : "<cartId>",
            |  "version" : <cartVersion>
            |}
            |""".stripMargin)
      Then I save_body_path("id" -> "orderFromCartId")
      Then I save_body_path("version" -> "orderFromCartVersion")
      Then assert status.is(201)
    }
}

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

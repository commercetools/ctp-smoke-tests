package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutExistProdTest extends CheckoutTest with FeatureWithToken {

  override lazy val baseUrl = apiUrl + s"/$projectKey"

  override def feature: FeatureDef = Feature("Checkout process for existing product") {
    Scenario("Select existing product | Create cart | Add LineItem | Create order | Delete order | Delete cart") {
      WithToken {
        Then assert queryProducts
        Then assert createCart          //def in CheckoutTest
        Then assert addLineItem
        Then assert createOrderFromCart
        Then assert getCartById         //def in CheckoutTest
        Then assert deleteOrderFromCart //def in CheckoutTest
        Then assert deleteCart          //def in CheckoutTest
      }
    }
  }

  def queryProducts = {
    Attach {
      When I get("/product-projections").withParams(

        "staged" -> "false",
        "where" -> "masterVariant(id is defined)",
        "where" -> "masterVariant(prices(id is defined))",
        "where" -> "taxCategory(id is defined)"
      )
      Then I save_body_path("results[0].id" -> "productId")
      Then assert status.is(200)
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
          |
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

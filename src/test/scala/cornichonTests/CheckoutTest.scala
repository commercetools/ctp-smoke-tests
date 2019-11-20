package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutTest extends FeatureWithToken {

  def feature: FeatureDef = Feature("Adding item to a cart and ordering the cart") {
    Scenario("Adding item / making order") {
      WithToken {
        Then assert createCart
        Then assert createTaxCategory
        Then assert addCustomLineItem
        Then assert createOrderFromCart
        Then assert deleteOrderFromCart

        //TODO delete line item
        //TODO delete tax category
        //TODO delete cart

        And I show_last_body_json
      }
    }
  }

  def createCart =
    Attach {
      When I post(s"$apiUrl/$projectKey/carts").withBody(
        """
          |{
          |  "currency" : "EUR",
          |  "shippingAddress" : {
          |  	"country" : "DE"
          |  }
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "cartId")
      Then I save_body_path("version" -> "cartVersion")
      Then assert status.is(201)
    }

  def createTaxCategory =
    Attach {
      When I post(s"$apiUrl/$projectKey/tax-categories").withBody(
        """
          |{
          |  "name" : "test-tax-category-<random-uuid>",
          |  "rates" : [ {
          |    "name" : "test-tax-category",
          |    "amount" : 0.2,
          |    "includedInPrice" : true,
          |    "country" : "DE"
          |  } ]
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "taxCategoryId")
      Then assert status.is(201)
    }

  def addCustomLineItem=
    Attach {
      When I post(s"$apiUrl/$projectKey/carts/<cartId>").withBody(
        """
          |{
          |    "version": <cartVersion>,
          |    "actions": [
          |        {
          |            "action" : "addCustomLineItem",
          |            "name" : {
          |              "en" : "Name EN",
          |              "de" : "Name DE"
          |            },
          |            "money" : {
          |              "currencyCode" : "EUR",
          |              "centAmount" : 4200
          |            },
          |            "slug" : "testSlug",
          |            "taxCategory" : {
          |              "typeId" : "tax-category",
          |              "id" : "<taxCategoryId>"
          |            }
          |          }
          |    ]
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "customLineItemId")
      Then I save_body_path("version" -> "customItemVersion")
      Then assert status.is(200)
    }

  def createOrderFromCart =
    Attach {
      When I post(s"$apiUrl/$projectKey/orders").withBody(
        """
          |{
          |  "id" : "<cartId>",
          |  "version" : <customItemVersion>
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "orderFromCartId")
      Then I save_body_path("version" -> "orderFromCartVersion")
      Then assert status.is(201)
    }

  def deleteOrderFromCart =
    Attach {
      When I delete(s"$apiUrl/$projectKey/orders/<orderFromCartId>").withParams("version" -> "<orderFromCartVersion>")
      Then assert status.is(200)
    }

}

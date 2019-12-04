package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutTest extends FeatureWithToken {

  override lazy val baseUrl = apiUrl + s"/$projectKey"

  def feature: FeatureDef = Feature("Checkout process") {
    Scenario(
      """Create cart | Create tax category | Add line item |
        | Create order | Delete order | Delete cart |
        | Delete tax category
        | """.stripMargin.replaceAll("\n","")) {
      WithToken {
        Then assert createCart
        Then assert createTaxCategory
        Then assert addCustomLineItem
        Then assert createOrderFromCart
        Then assert getCartById
        Then assert deleteOrderFromCart
        Then assert deleteCart
        Then assert deleteTaxCategory
      }
    }
  }

  def createCart =
    Attach {
      When I post("/carts")
        .withBody("""
          |{
          |  "currency" : "EUR",
          |  "shippingAddress" : {
          |  	"country" : "DE"
          |  }
          |}
          |""".stripMargin)
      Then I save_body_path("id" -> "cartId")
      Then I save_body_path("version" -> "cartVersion")
      Then assert status.is(201)
    }

  def createTaxCategory =
    Attach {
      When I post("/tax-categories")
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

  def addCustomLineItem =
    Attach {
      When I post("/carts/<cartId>")
        .withBody("""
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
          |""".stripMargin)
      Then I save_body_path("version" -> "customLineItemVersion")
      Then assert status.is(200)
    }

  def createOrderFromCart =
    Attach {
      When I post("/orders")
        .withBody("""
          |{
          |  "id" : "<cartId>",
          |  "version" : <customLineItemVersion>
          |}
          |""".stripMargin)
      Then I save_body_path("id" -> "orderFromCartId")
      Then I save_body_path("version" -> "orderFromCartVersion")
      Then assert status.is(201)
    }

  def getCartById =
    Attach {
      When I get("/carts/<cartId>")
      Then I save_body_path("version" -> "cartVersion")
      Then assert status.is(200)
    }

  def deleteOrderFromCart =
    Attach {
      When I delete("/orders/<orderFromCartId>")
        .withParams("version" -> "<orderFromCartVersion>")
      Then assert status.is(200)
    }

  def deleteCart =
    Attach {
      When I delete("/carts/<cartId>")
        .withParams("version" -> "<cartVersion>")
      Then assert status.is(200)
    }

  def deleteTaxCategory =
    Attach {
      When I delete("/tax-categories/<taxCategoryId>/")
        .withParams("version" -> "<taxCategoryVersion>")
      Then assert status.is(200)
    }
}

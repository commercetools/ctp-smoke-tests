package cornichonTests
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutExistProdTest extends AddProduct {
  override def feature: FeatureDef = Feature("Checkout process for existing product") {
    Scenario("Add product | Create order | Cleanup") {
      Then assert session_value("orderFromCartId").isPresent
    }
  }
}

trait AddProduct extends CheckoutTest with CornichonFeature with FeatureWithToken with FeatureWithProduct {
  override lazy val baseUrl = apiUrl + s"/$projectKey"

  beforeEachScenario(
    Attach{
      WithToken {
        createTaxCategory
        addProductType
        addProduct
        createCart
        addLineItem
        createOrderFromCart
        getOrderFromCart
      }
    }
  )

  afterEachScenario(
    Attach{
      WithToken {
        getCartById
        deleteOrderFromCart
        deleteCart
        unpublishProduct
        deleteProduct
        deleteProductType
      }
    }
  )

  def addLineItem =
    Attach {
      When I post("/carts/<cartId>").withBody("""
        |{
        |   "version": <cartVersion>,
        |   "actions": [
        |     {
        |       "action" : "addLineItem",
        |       "productId" : "<productId>"
        |     },
        |     {
        |       "action" : "setShippingAddress",
        |       "address": {
        |         "key": "test_address",
        |         "company": "commercetools GmbH",
        |         "streetName": "Sonnenallee",
        |         "streetNumber": "223",
        |         "city": "Berlin",
        |         "postalCode": "12059",
        |         "country": "DE"
        |       }
        |     }
        |   ]
        |}
        |""".stripMargin)
      Then I save_body_path("version" -> "cartVersion")
      And assert status.is(200)
    }

  override def createOrderFromCart =
    Attach {
      When I post("/orders").withBody("""
           |{
           |   "id" : "<cartId>",
           |   "version" : <cartVersion>
           |}
           |""".stripMargin)
      Then I save_body_path("id" -> "orderFromCartId")
      Then I save_body_path("version" -> "orderFromCartVersion")
      Then assert status.is(201)
    }

  def getOrderFromCart =
    Attach {
      When I get("/orders/<orderFromCartId>")
      Then assert status.is(200)
    }
}


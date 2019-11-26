package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutExistProdTest extends FeatureWithToken {

  def feature: FeatureDef = Feature("Checkout process") {
    Scenario("Select item from existing product | Make an order | Delete order") {
      WithToken {
        Then assert queryProducts
        Then assert getProductProjectionByID
        And I show_last_body_json
      }
    }
  }

  def queryProducts = {
    Attach {
      When I get(s"$apiUrl/$projectKey/product-projections/search").withParams("staged" -> "false")
      Then assert status.is(200)
      Then assert body.path("count").is("*any-positive-integer*")
      Then I save_body_path("results[0].id" -> "productId")
    }
  }

  def getProductProjectionByID = {
    Attach {
      When I get(s"$apiUrl/$projectKey/product-projections/<productId>")
      Then assert body.path("taxCategory").isPresent

      //TODO check if price in masterVariant is present
      //TODO add line item from existing product
      //TODO make an order from line item
      //TODO unpublish product
      //TODO delete order form cart
      //TODO delete cart

      Then assert status.is(200)
    }
  }
}

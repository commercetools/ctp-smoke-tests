package cornichonTests
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.core.FeatureDef
import com.github.agourlay.cornichon.http.{HttpRequest, RootExtractor}
import com.github.agourlay.cornichon.steps.regular.EffectStep

class CheckoutTest extends CornichonFeature with FeatureWithProduct {

  def feature: FeatureDef = Feature("Checkout process with custom line item") {
    Scenario("Add custom line item | Create order") {
      Given a newTaxCategory
      Given a newCart
      When  I addCustomLineItem
      And   I createOrderFromCart
      Then  I getCartById
      And   I getOrderFromCart
    }
  }

  def addCustomLineItem =
    Attach {
      WithToken {
        EffectStep(
          title = "adding custom line item",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/carts/<cart-id>")
              .withBody("""
                  |{
                  |   "version" : <cart-version>,
                  |   "actions" : [
                  |     {
                  |       "action" : "addCustomLineItem",
                  |       "name" : {
                  |         "en" : "Name EN",
                  |         "de" : "Name DE"
                  |       },
                  |       "money" : {
                  |         "currencyCode" : "EUR",
                  |         "centAmount" : 4200
                  |       },
                  |       "slug" : "testSlug",
                  |       "taxCategory" : {
                  |         "typeId" : "tax-category",
                  |         "id" : "<tax-category-id>"
                  |       }
                  |     }
                  |   ]
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("cart"),
            expectedStatus = Some(200)
          )
        )
      }
    }
}

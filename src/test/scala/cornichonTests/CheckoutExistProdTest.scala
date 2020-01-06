package cornichonTests
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.core.FeatureDef

class CheckoutExistProdTest extends CornichonFeature with FeatureWithProduct {

  override def feature: FeatureDef = Feature("Checkout process for existing product") {
    Scenario("Add product | Add LineItem | Create order") {
      Given a newTaxCategory
      Given a newProductType
      Given a newProduct
      Given a newCart
      When  I addLineItem
      And   I createOrderFromCart
      And   I getOrderFromCart
      And   I getCartById
      Then  assert status.is(200)
    }
  }
}


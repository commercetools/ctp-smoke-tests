package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef
import scala.concurrent.duration._

class FindProductTest extends FeatureWithProduct {

  def feature: FeatureDef = Feature("Full-text product search") {
    Scenario("Add product | Get projection | Full-text search") {
      Given a newTaxCategory
      Given a newProductType
      Given a newProduct
      Then  I getProductProjectionById

      Eventually(maxDuration = 60.seconds, interval = 100.milliseconds){
        When I fullTextProductSearch
        Then assert body.path("results[0].slug.en").is("<product-slug>")
      }
    }
  }
}

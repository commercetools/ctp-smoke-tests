package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.resolver.Mapper

trait FeatureWithToken extends CornichonFeature with FeatureConfig with AuthSteps {

  beforeEachScenario(
    Attach {
      basic_auth_request
    }
  )
  override def registerExtractors: Map[String, Mapper] = super.registerExtractors ++ AuthSteps.registerExtractors
}

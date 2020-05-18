package cornichonTests

import java.util.UUID

import cats.effect.syntax.effect
import cats.syntax.show
import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.resolver.Mapper
import com.github.agourlay.cornichon.steps.regular.EffectStep

trait FeatureWithToken extends CornichonFeature with FeatureConfig with AuthSteps {

  beforeEachScenario(
    Attach {
      basic_auth_request
      setupCorrelationIdHeader
    }
  )
  override def registerExtractors: Map[String, Mapper] = super.registerExtractors ++ AuthSteps.registerExtractors

  private def setupCorrelationIdHeader =
    EffectStep.fromSyncE(
      title = "Header setup for X-Correlation-ID",
      show = false,
      effect = scenarioContext => {
        val correlationId = s"${this.getClass.getName.replace(".", "_")}-${UUID.randomUUID().toString}".toLowerCase
        for {
          s1 <- scenarioContext.session.addValue("correlation-id", correlationId)
          s2 <- addToWithHeaders("x-correlation-id", correlationId)(s1)
        } yield s2
      }
    )
}

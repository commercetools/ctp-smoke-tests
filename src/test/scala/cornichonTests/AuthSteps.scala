package cornichonTests

import java.nio.charset.StandardCharsets
import java.util.Base64

import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.http.{HttpRequest, RootExtractor}
import com.github.agourlay.cornichon.resolver.JsonMapper
import com.github.agourlay.cornichon.steps.regular.EffectStep

trait AuthSteps {
  this: CornichonFeature with FeatureConfig =>
  import AuthSteps._

  lazy val auth = httpServiceByURL(authUrl)

  def WithToken = WithHeaders("Authorization" -> "Bearer <oauth-token>")

  def basic_auth_request = EffectStep(
    title = "sending API auth request",
    effect = { s =>
      val effect = auth.requestEffect(
        request = HttpRequest
          .post("")
          .withParams("grant_type" -> "client_credentials", "scope" -> s"manage_project:$projectKey")
          .withHeaders("Authorization" -> basicAuthHeaders(clientId, clientSecret)),
        extractor = RootExtractor("oauth-token-extract"),
        expectedStatus = Some(200)
      )
      effect(s)
    }
  )
}

object AuthSteps {
  def registerExtractors = Map("oauth-token" -> JsonMapper("oauth-token-extract", "access_token"))

  def basicAuthHeaders(clientId: String, clientSecret: String) =
    "Basic " + Base64.getEncoder.encodeToString(s"$clientId:$clientSecret".getBytes(StandardCharsets.UTF_8))
}

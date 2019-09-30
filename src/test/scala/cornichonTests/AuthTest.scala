package cornichonTests
import com.github.agourlay.cornichon.CornichonFeature
import eu.timepit.refined.api.Refined
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.string.Url
import pureconfig.generic.auto._
import pureconfig._

class AuthTest extends CornichonFeature with FeatureConfig {
  def feature = Feature("checking the API request") {
    Scenario("API request returns 200") {

      WithBasicAuth(clientId, clientSecret) {
        When I post(authUrl)
        .withParams(
          "grant_type" -> "client_credentials",
          "scope" -> s"manage_project:${projectKey}")

        Then assert status.is(200)
        And I show_last_body_json
      }
    }
  }
}


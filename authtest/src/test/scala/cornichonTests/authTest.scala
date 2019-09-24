package cornichonTests
import com.github.agourlay.cornichon.CornichonFeature

class authTest extends CornichonFeature with FeatureConfig {
  def feature = Feature("checking the API request"){
    Scenario("API request returns 200"){

      WithBasicAuth (configuration.clientId,configuration.clientSecret){
        When I post(s"$authUrl")
        .withParams("grant_type" -> "client_credentials", "scope" -> s"manage_project:$projectKey")

        Then assert status.is(200)
        And I show_last_body_json
      }
    }
  }
}
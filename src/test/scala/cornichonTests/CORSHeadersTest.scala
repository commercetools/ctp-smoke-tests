package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature

class CORSHeadersTest extends CornichonFeature with FeatureConfig {

  override def feature = Feature("CORS Headers") {
    Scenario("be emitted on requests on auth-ws") {
      When I get(authUrl)
      Then assert status.is(200)
      check_CORS_headers()
    }

    Scenario("be emitted on requests on projects-ws") {
      When I get(apiUrl)
      Then assert status.is(200)
      check_CORS_headers()
    }
  }

  private def check_CORS_headers() = Attach {
    And assert headers.name("Access-Control-Allow-Origin").isPresent
    And assert headers.name("Access-Control-Allow-Headers").isPresent
    And assert headers.name("Access-Control-Allow-Methods").isPresent
    And assert headers.name("Access-Control-Max-Age").isPresent
  }
}

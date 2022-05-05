package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature

class CORSHeadersTest extends CornichonFeature with FeatureConfig {

  override def feature = Feature("CORS Headers") {
    Scenario("be emitted on requests on auth-ws") {
      check_CORS_headers(authUrl)
    }

    Scenario("be emitted on requests on projects-ws") {
      check_CORS_headers(apiUrl)
    }
  }

  private def check_CORS_headers(url: String) = Attach {
    When I options(url)
    Then assert status.isSuccess
    Then assert headers.name("Access-Control-Allow-Origin").isPresent
    Then assert headers.name("Access-Control-Allow-Headers").isPresent
    Then assert headers.name("Access-Control-Allow-Methods").isPresent
    Then assert headers.name("Access-Control-Max-Age").isPresent

    When I get(url)

    And assert headers.name("Access-Control-Allow-Origin").isPresent
    And assert headers.name("Access-Control-Allow-Headers").isPresent
    And assert headers.name("Access-Control-Allow-Methods").isPresent
    And assert headers.name("Access-Control-Max-Age").isPresent
  }
}

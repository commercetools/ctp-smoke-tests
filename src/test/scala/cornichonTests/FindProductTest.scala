package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.core.FeatureDef
import com.github.agourlay.cornichon.http.HttpRequest
import com.github.agourlay.cornichon.steps.cats.EffectStep
import cornichonTests.FeatureConfig

class FindProductTest extends CornichonFeature with FeatureConfig {
  def save_token = EffectStep(
  val effect = http.requestEffect(

    //TODO handle auth request

    request = HttpRequest.post(authUrl)

  def feature: FeatureDef = Feature("Adding a product with a product type" ){
    Scenario("Adding a product type") {
       WithHeaders {("Authorization" -> "Bearer <oauth-token>")
        When  I post(s"$apiUrl/$projectKey/products").

          //TODO only add the type here without the auth

          withParams("grant_type" -> "client_credentials", "scope" -> s"manage_project:$projectKey").
          withBody(
            """
              |{
              |    "name": "<random-string>",
              |    "description": "<random-string>",
              |    "attributes": [
              |        {
              |            "name": "<random-string>",
              |            "type": {
              |                "name": "<random-string>"
              |            },
              |            "isRequired": false,
              |            "isSearchable": true,
              |            "label": {
              |                "en": "<random-string>"
              |            },
              |            "attributeConstraint": "Unique"
              |        }
              |    ]
              |}
              |""".stripMargin
          )
        Then assert status.is(201)

         //TODO add a product
         //TODO product projection search
         //TODO full text search
         //TODO delete product
         //TODO delete product type

      }
    }
  }

}




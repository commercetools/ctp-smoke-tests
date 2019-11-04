package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class FindProductTest extends FeatureWithToken {

  def feature: FeatureDef = Feature("Adding a product with a product type") {
    Scenario("find a product") {
      WithToken {
        Then assert addProductType
        Then assert addProduct
        And I show_last_body_json

        //TODO product projection search
        //TODO full text search
        //TODO delete product
        //TODO delete product type
      }
    }
  }

  def addProductType =
    Attach {
      When I post(s"$apiUrl/$projectKey/product-types").withBody(
        """
          |{
          |    "name": "test_product_type",
          |    "description": "Test product type.",
          |    "attributes": [
          |        {
          |            "name": "some_attribute_name",
          |            "type": {
          |                "name": "text"
          |            },
          |            "isRequired": false,
          |            "isSearchable": true,
          |            "label": {
          |                "en": "some label"
          |            },
          |            "attributeConstraint": "None"
          |        }
          |    ]
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "productTypeId")
      Then assert status.is(201)
    }

  def addProduct =
    Attach {
      When I post(s"$apiUrl/$projectKey/products").withBody(
        """
          |{
          |  "productType" : {
          |    "id" : "<productTypeId>",
          |    "typeId" : "product-type"
          |  },
          |  "name" : {
          |    "en" : "Some Product"
          |  },
          |  "slug" : {
          |    "en" : "product_slug_<random-uuid>"
          |  }
          |}
          |""".stripMargin
      )
    }
}

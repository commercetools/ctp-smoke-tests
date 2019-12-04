package cornichonTests
import com.github.agourlay.cornichon.core.FeatureDef

class FindProductTest extends FeatureWithToken {

  override lazy val baseUrl = apiUrl + s"/$projectKey"

  def feature: FeatureDef = Feature("Full-text product search") {
    Scenario("Add product type | Add product | Find product | Delete product") {

      WithToken {
        Then assert addProductType
        Then assert addProduct
        Then assert getProductProjection
        Then assert fullTextProductSearch
        Then assert unpublishProduct
        Then assert deleteProduct
        Then assert deleteProductType
      }
    }
  }

  def addProductType =
    Attach {
      When I post("/product-types").withBody(
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
      Then I save_body_path("id" -> "productTypeId", "version" -> "productTypeVersion")
      Then assert status.is(201)
    }

  def addProduct =
    Attach {
      When I post("/products").withBody(
        """
          |{
          |  "productType" : {
          |    "id" : "<productTypeId>",
          |    "typeId" : "product-type"
          |  },
          |  "name" : {
          |    "en" : "Test Product"
          |  },
          |  "slug" : {
          |    "en" : "product_slug_<random-uuid>"
          |  },
          |  "publish": true
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "productId", "version" -> "productVersion")
      Then assert status.is(201)
    }

  def getProductProjection =
    Attach {
      When I get("/product-projections/<productId>")
      Then assert status.is(200)
    }

  def fullTextProductSearch =
    Attach {
      When I get("/product-projections").
        withParams("name" -> "Test Product", "filter" -> "productType.id:<productTypeId>")
      Then assert status.is(200)
    }

  def unpublishProduct =
    Attach {
      When I post("/products/<productId>").withBody("""
          |{
          |    "version": <productVersion>,
          |    "actions": [
          |        {
          |            "action" : "unpublish"
          |        }
          |    ]
          |}
          |""".stripMargin)
      Then assert body.path("masterData.published").is(false)
      Then I save_body_path("version" -> "productVersion")
      Then assert status.is(200)
    }

  def deleteProduct =
    Attach {
      When I delete("/products/<productId>").
        withParams("version" -> "<productVersion>")
      Then assert status.is(200)
    }

  def deleteProductType =
    Attach {
      When I delete("/product-types/<productTypeId>").
        withParams("version" -> "<productTypeVersion>")
      Then assert status.is(200)
    }
}

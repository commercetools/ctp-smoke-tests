package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature
import com.github.agourlay.cornichon.http.{HttpRequest, RootExtractor}
import com.github.agourlay.cornichon.resolver.{JsonMapper, Mapper}
import com.github.agourlay.cornichon.steps.regular.EffectStep
import com.github.agourlay.cornichon.steps.wrapped.ScenarioResourceStep

trait FeatureWithProduct extends FeatureWithToken {
  this: CornichonFeature with FeatureConfig =>

  override def registerExtractors: Map[String, Mapper] = FeatureWithProduct.extractors ++ AuthSteps.registerExtractors

  lazy val baseUrlHttp = httpServiceByURL(s"$apiUrl" + s"/$projectKey")

  def newTaxCategory = ScenarioResourceStep(
    title = "add new tax category",
    acquire = addTaxCategory,
    release = deleteTaxCategory
  )

  def queryTaxCategories =
    Attach {
      WithToken{
        EffectStep(
          title = "query tax cats",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
                .get("/tax-categories"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def addTaxCategory =
    Attach {
      WithToken {
        EffectStep(
          title = "create tax category",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/tax-categories")
              .withBody(
                """
                  |{
                  |  "name" : "test-tax-category-<random-positive-integer>",
                  |  "rates" : [ {
                  |    "name" : "test-tax-category",
                  |    "amount" : 0.2,
                  |    "includedInPrice" : true,
                  |    "country" : "DE"
                  |  } ]
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("tax-category"),
            expectedStatus = Some(201)
          )
        )
      }
    }

  def deleteTaxCategory =
    Attach {
      WithToken {
        EffectStep(
          title = "deleting tax category",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .delete(s"/tax-categories/<tax-category-id>")
              .withParams("version" -> s"<tax-category-version>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  object FeatureWithProduct {
    val extractors = Map(
      "tax-category-id" -> JsonMapper("tax-category", "id"),
      "tax-category-version" -> JsonMapper("tax-category", "version")
    )
  }

}

//  def addProductType=
//    Attach {
//      When I post("/product-types").withBody(
//        """
//          |{
//          |   "name": "test_product_type",
//          |   "description": "Test product type.",
//          |   "attributes": [
//          |     {
//          |       "name": "some_attribute_name",
//          |       "type": {
//          |       "name": "text"
//          |       },
//          |       "isRequired": false,
//          |       "isSearchable": true,
//          |       "label": {
//          |       "en": "some label"
//          |       },
//          |       "attributeConstraint": "None"
//          |     }
//          |   ]
//          |}
//          |""".stripMargin
//      )
//      Then I save_body_path("id" -> "productTypeId", "version" -> "productTypeVersion")
//      Then assert status.is(201)
//    }
//
//  def addProduct =
//    Attach {
//      When I post("/products").withBody(
//        """
//          |{
//          |   "productType" : {
//          |     "id" : "<productTypeId>",
//          |     "typeId" : "product-type"
//          |   },
//          |   "name" : {
//          |    "en" : "Some Product"
//          |   },
//          |   "slug" : {
//          |     "en" : "product_slug_<random-positive-integer>"
//          |   },
//          |   "publish": true,
//          |   "masterVariant" : {
//          |     "sku" : "SKU-<random-positive-integer>",
//          |     "prices" : [
//          |       {
//          |         "value" : {
//          |           "type": "centPrecision",
//          |           "currencyCode" : "EUR",
//          |           "centAmount" : 4200
//          |         }
//          |       }
//          |     ]
//          |   },
//          |   "taxCategory" : {
//          |     "typeId" : "tax-category",
//          |     "id" : "<taxCategoryId>"
//          |   }
//          |}
//          |""".stripMargin
//      )
//      Then I save_body_path("id" -> "productId", "version" -> "productVersion")
//      Then assert status.is(201)
//    }
//
//  def unpublishProduct =
//    Attach {
//      When I post("/products/<productId>").withBody(
//        """
//          |{
//          |   "version": <productVersion>,
//          |   "actions": [
//          |     {
//          |       "action" : "unpublish"
//          |     }
//          |   ]
//          |}
//          |""".stripMargin)
//
//      Then I save_body_path("version" -> "productVersion")
//    }
//
//  def deleteProduct =
//    Attach {
//      When I delete("/products/<productId>").
//        withParams("version" -> "<productVersion>")
//      Then assert status.is(200)
//    }
//
//  def deleteProductType =
//    Attach {
//      When I delete("/product-types/<productTypeId>").
//        withParams("version" -> "<productTypeVersion>")
//      Then assert status.is(200)
//    }


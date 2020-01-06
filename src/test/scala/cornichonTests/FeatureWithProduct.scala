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

  def newProductType = ScenarioResourceStep(
    title = "add new product type",
    acquire = addProductType,
    release = deleteProductType
  )

  def newProduct = ScenarioResourceStep(
    title = "add new product",
    acquire = addProduct,
    release = deleteProduct
  )

  def newCart = ScenarioResourceStep(
    title = "add new cart",
    acquire = createCart,
    release = deleteCart
  )

  def createOrderFromCart = ScenarioResourceStep(
    title = "create order from cart",
    acquire = createOrder,
    release = deleteOrder
  )

  def queryTaxCategories =
    Attach {
      WithToken {
        EffectStep(
          title = "query tax cats",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest.get("/tax-categories"),
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
              .withBody("""
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

  def addProductType =
    Attach {
      WithToken {
        EffectStep(
          title = "adding type category",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/product-types")
              .withBody("""
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
                    |""".stripMargin),
            extractor = RootExtractor("product-type"),
            expectedStatus = Some(201)
          )
        )
      }
    }

  def deleteProductType =
    Attach {
      WithToken {
        EffectStep(
          title = "deleting product type",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .delete(s"/product-types/<product-type-id>")
              .withParams("version" -> s"<product-type-version>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def addProduct =
    Attach {
      WithToken {
        EffectStep(
          title = "adding product",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/products")
              .withBody("""
                  |{
                  |  "productType" : {
                  |    "id" : "<product-type-id>",
                  |    "typeId" : "product-type"
                  |  },
                  |  "name" : {
                  |    "en" : "Test Product"
                  |  },
                  |  "slug" : {
                  |    "en" : "product_slug_<random-uuid>"
                  |  },
                  |  "publish" : true,
                  |  "masterVariant" : {
                  |     "sku" : "SKU-<random-positive-integer>",
                  |     "prices" : [
                  |       {
                  |         "value" : {
                  |           "type": "centPrecision",
                  |           "currencyCode" : "EUR",
                  |           "centAmount" : 4200
                  |         }
                  |       }
                  |     ]
                  |  },
                  |  "taxCategory" : {
                  |     "typeId" : "tax-category",
                  |     "id" : "<tax-category-id>"
                  |  }
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("product"),
            expectedStatus = Some(201)
          )
        )
      }
    }

  def unpublishProduct =
    Attach {
      WithToken {
        EffectStep(
          title = "unpublishing product",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post(s"/products/<product-id>")
              .withBody("""
                  |{
                  |   "version": <product-version>,
                  |   "actions": [
                  |     {
                  |       "action" : "unpublish"
                  |     }
                  |   ]
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("product"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def deleteProductById =
    Attach {
      WithToken {
        EffectStep(
          title = "deleting product",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .delete(s"/products/<product-id>")
              .withParams("version" -> s"<product-version>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def deleteProduct =
    Attach {
      unpublishProduct
      deleteProductById
    }

  def createCart =
    Attach {
      WithToken {
        EffectStep(
          title = "create cart",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/carts")
              .withBody("""
                  |{
                  |  "currency" : "EUR",
                  |  "shippingAddress" : {
                  |  	"country" : "DE"
                  |  }
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("cart"),
            expectedStatus = Some(201)
          )
        )
      }
    }

  def deleteCart =
    Attach {
      WithToken {
        EffectStep(
          title = "deleting cart",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .delete(s"/carts/<cart-id>")
              .withParams("version" -> s"<cart-version>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def addLineItem =
    Attach {
      WithToken {
        EffectStep(
          title = "adding line item",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/carts/<cart-id>")
              .withBody("""
                  |{
                  |   "version": <cart-version>,
                  |   "actions": [
                  |     {
                  |       "action" : "addLineItem",
                  |       "productId" : "<product-id>"
                  |     },
                  |     {
                  |       "action" : "setShippingAddress",
                  |       "address": {
                  |         "key": "test_address",
                  |         "company": "commercetools GmbH",
                  |         "streetName": "Sonnenallee",
                  |         "streetNumber": "223",
                  |         "city": "Berlin",
                  |         "postalCode": "12059",
                  |         "country": "DE"
                  |       }
                  |     }
                  |   ]
                  |}
                  |""".stripMargin),
            extractor = RootExtractor("cart"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def createOrder =
    Attach {
      WithToken {
        EffectStep(
          title = "create order",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .post("/orders")
              .withBody("""
                |{
                |   "id" : "<cart-id>",
                |   "version" : <cart-version>
                |}
                |""".stripMargin),
            extractor = RootExtractor("order"),
            expectedStatus = Some(201)
          )
        )
      }
    }

  def deleteOrder =
    Attach {
      WithToken {
        EffectStep(
          title = "delete order",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest
              .delete("/orders/<order-id>")
              .withParams("version" -> "<order-version>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def getOrderFromCart =
    Attach {
      WithToken {
        EffectStep(
          title = "query order by id",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest.get("/orders/<order-id>"),
            expectedStatus = Some(200)
          )
        )
      }
    }

  def getCartById =
    Attach {
      WithToken {
        EffectStep(
          title = "query cart by id",
          effect = baseUrlHttp.requestEffect(
            request = HttpRequest.get("/carts/<cart-id>"),
            extractor = RootExtractor("cart"),
            expectedStatus = Some(200)
          )
        )
      }
    }

}

object FeatureWithProduct {

  val extractors = Map(
    "tax-category-id"      -> JsonMapper("tax-category", "id"),
    "tax-category-version" -> JsonMapper("tax-category", "version"),
    "product-type-id"      -> JsonMapper("product-type", "id"),
    "product-type-version" -> JsonMapper("product-type", "version"),
    "product-id"           -> JsonMapper("product", "id"),
    "product-version"      -> JsonMapper("product", "version"),
    "cart-id"              -> JsonMapper("cart", "id"),
    "cart-version"         -> JsonMapper("cart", "version"),
    "order-id"             -> JsonMapper("order", "id"),
    "order-version"        -> JsonMapper("order", "version")
  )
}

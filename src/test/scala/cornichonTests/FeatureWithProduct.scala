package cornichonTests

import com.github.agourlay.cornichon.CornichonFeature

trait FeatureWithProduct extends FeatureWithToken {
  this: CornichonFeature =>

  def AddProduct =
    Attach {
      addProductType
      addProduct
    }

  def DeleteProduct =
    Attach {
      unpublishProduct
      deleteProduct
      deleteProductType
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
          |         "name": "some_attribute_name",
          |          "type": {
          |              "name": "text"
          |          },
          |          "isRequired": false,
          |          "isSearchable": true,
          |          "label": {
          |              "en": "some label"
          |          },
          |          "attributeConstraint": "None"
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
         {
          |  "productType" : {
          |    "id" : "<productTypeId>",
          |    "typeId" : "product-type"
          |  },
          |  "name" : {
          |    "en" : "Some Product"
          |  },
          |  "slug" : {
          |    "en" : "product_slug_<random-positive-integer>"
          |  },
          |   "publish": true,
          |   "masterVariant" : {
          |    	"sku" : "SKU-<random-positive-integer>",
          |    	"prices" : [
          |    		{"value" : {
          |    			"type": "centPrecision",
          |    			"currencyCode" : "EUR",
          |    			"centAmount" : 4200
          |    			}
          |    		}
          |    	]
          |   },
          |   "taxCategory" : {
          |    	"typeId" : "tax-category",
          |        "id" : "<taxCategoryId>"
          |    }
          |}
          |""".stripMargin
      )
      Then I save_body_path("id" -> "productId", "version" -> "productVersion")
      Then assert status.is(201)
    }

  def unpublishProduct =
    Attach {
      When I post("/products/<productId>").withBody(
        """
          |{
          |    "version": <productVersion>,
          |    "actions": [
          |       {
          |          "action" : "unpublish"
          |       }
          |    ]
          |}
          |""".stripMargin)

      Then I save_body_path("version" -> "productVersion")
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


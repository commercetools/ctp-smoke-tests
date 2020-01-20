package cornichonTests

class AuthTest extends FeatureWithToken {

  def feature = Feature("Checking the API request") {

    Scenario("return 200 for the API request") {
      Then asssert body.path("access_token").isPresent
    }
  }
}

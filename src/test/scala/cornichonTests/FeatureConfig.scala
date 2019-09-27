package cornichonTests

import eu.timepit.refined.api.Refined
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.string.Url
import pureconfig.generic.auto._
import pureconfig._

case class Config(
  authUrl: String Refined Url,
  clientId: String,
  clientSecret: String,
  projectKey: String,
)

trait FeatureConfig {
  lazy val configuration: Config = ConfigSource.default.at("auth-test-vars").loadOrThrow[Config]
  lazy val clientId = configuration.clientId
  lazy val clientSecret = configuration.clientSecret
  lazy val authUrl = configuration.authUrl.value
  lazy val projectKey = configuration.projectKey
}


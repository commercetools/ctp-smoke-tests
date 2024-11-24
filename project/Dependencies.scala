import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.21.4" ::
      "eu.timepit" %% "refined" % "0.11.2" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.2" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.8" ::
      Nil map (_ % Test)
}

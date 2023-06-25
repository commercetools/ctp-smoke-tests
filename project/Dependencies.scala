import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.20.8" ::
      "eu.timepit" %% "refined" % "0.11.0" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.0" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.4" ::
      Nil map (_ % Test)
}

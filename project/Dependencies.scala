import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.22.0" ::
      "eu.timepit" %% "refined" % "0.11.3" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.3" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.8" ::
      Nil map (_ % Test)
}

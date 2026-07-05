import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.24.0" ::
      "eu.timepit" %% "refined" % "0.11.4" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.4" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.10" ::
      Nil map (_ % Test)
}

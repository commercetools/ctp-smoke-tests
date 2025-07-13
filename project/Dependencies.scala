import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.22.1" ::
      "eu.timepit" %% "refined" % "0.11.3" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.3" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.9" ::
      Nil map (_ % Test)
}

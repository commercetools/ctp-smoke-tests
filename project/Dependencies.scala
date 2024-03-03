import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.21.2" ::
      "eu.timepit" %% "refined" % "0.11.1" ::
      "eu.timepit" %% "refined-pureconfig" % "0.11.1" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.6" ::
      Nil map (_ % Test)
}

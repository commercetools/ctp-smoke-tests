import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.20.6" ::
      "eu.timepit" %% "refined" % "0.10.1" ::
      "eu.timepit" %% "refined-pureconfig" % "0.10.1" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.2" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.2" ::
      Nil map (_ % Test)
}

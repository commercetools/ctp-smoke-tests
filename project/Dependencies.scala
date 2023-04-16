import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.20.7" ::
      "eu.timepit" %% "refined" % "0.10.2" ::
      "eu.timepit" %% "refined-pureconfig" % "0.10.2" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.3" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.3" ::
      Nil map (_ % Test)
}

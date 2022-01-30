import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.20.1" ::
      "eu.timepit" %% "refined" % "0.9.28" ::
      "eu.timepit" %% "refined-pureconfig" % "0.9.28" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.1" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.1" ::
      Nil map (_ % Test)
}

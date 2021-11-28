import sbt._

object Dependencies {

  lazy val smokeTests =
    "com.github.agourlay" %% "cornichon-test-framework" % "0.19.8" ::
      "eu.timepit" %% "refined" % "0.9.27" ::
      "eu.timepit" %% "refined-pureconfig" % "0.9.27" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.1" ::
      "com.github.pureconfig" %% "pureconfig" % "0.17.1" ::
      Nil map (_ % Test)
}

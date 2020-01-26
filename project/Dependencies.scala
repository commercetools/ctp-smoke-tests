import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

  lazy val smokeTests =
    "com.github.agourlay"     %% "cornichon-scalatest"      % "0.18.1" ::
      "eu.timepit"            %% "refined"                  % "0.9.12" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.12" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.2" ::
      Nil map (_ % Test)
}

import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

  lazy val smokeTests =
    "com.github.agourlay"     %% "cornichon-scalatest"      % "0.18.1" ::
      "eu.timepit"            %% "refined"                  % "0.9.10" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.10" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.1" ::
      Nil map (_ % Test)
}

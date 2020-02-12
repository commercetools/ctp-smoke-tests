import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.0" ::
      "eu.timepit"            %% "refined"                  % "0.9.10" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.10" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.1" ::
      Nil map (_ % Test)
}

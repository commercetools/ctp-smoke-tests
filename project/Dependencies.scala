import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.0" ::
      "eu.timepit"            %% "refined"                  % "0.9.13" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.13" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.12.3" ::
      Nil map (_ % Test)
}

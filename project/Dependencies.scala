import sbt._

object Dependencies {

  lazy val smokeTests =
      "com.github.agourlay"   %% "cornichon-test-framework" % "0.19.1" ::
      "eu.timepit"            %% "refined"                  % "0.9.14" ::
      "eu.timepit"            %% "refined-pureconfig"       % "0.9.14" ::
      "com.github.pureconfig" %% "pureconfig"               % "0.13.0" ::
      Nil map (_ % Test)
}
